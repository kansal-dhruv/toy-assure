package com.increff.ta.dto;

import com.increff.ta.api.*;
import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.OrderForm;
import com.increff.ta.commons.model.OrderItemForm;
import com.increff.ta.commons.model.OrderUploadCSV;
import com.increff.ta.commons.model.enums.OrderStatus;
import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.OrderDtoHelper;
import com.increff.ta.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderDto {

  private final OrderApi orderApi;

  private final UserApi userApi;

  private final ChannelApi channelApi;

  private final ProductApi productApi;

  private final InvoiceService invoiceService;

  private final OrderFlowApi orderFlowApi;

  @Autowired
  public OrderDto(OrderApi orderApi, UserApi userApi, ChannelApi channelApi, ProductApi productApi, InvoiceService invoiceService, OrderFlowApi orderFlowApi) {
    this.orderApi = orderApi;
    this.userApi = userApi;
    this.channelApi = channelApi;
    this.productApi = productApi;
    this.invoiceService = invoiceService;
    this.orderFlowApi = orderFlowApi;
  }

  public void createOrderCSV(Long clientId, String channelOrderId, Long customerId, MultipartFile csvFile) {
    List<OrderUploadCSV> orderUploadDetails = OrderDtoHelper.validateAndParseCSV(csvFile);
    UserPojo client = userApi.getUserById(clientId);
    UserPojo customer = userApi.getUserById(customerId);
    if (Objects.isNull(client) || Objects.isNull(customer)
        || !isClientAndCustomerValid(client, customer)) {
      throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
    }
    ChannelPojo defaultChannel = channelApi.getChannelByName("INTERNAL");
    if (Objects.isNull(defaultChannel)) {
      throw new ApiException(Constants.CODE_INTERNAL_CHANNEL_NOT_FOUND, Constants.MSG_INTERNAL_CHANNEL_NOT_FOUND);
    }
    if (Objects.nonNull(orderApi.getOrderByChannelOrderId(channelOrderId))) {
      throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
    }
    OrderPojo orderPojo = OrderDtoHelper.createOrderPojo(clientId, channelOrderId, defaultChannel, customerId);
    List<OrderItemPojo> orderItems = convertCsvToPojo(orderUploadDetails);
    orderApi.createOrder(orderPojo, orderItems);
  }


  public void createOrder(OrderForm orderForm) {
    UserPojo client = userApi.getUserById(orderForm.getClientId());
    UserPojo customer = userApi.getUserById(orderForm.getCustomerId());
    if (Objects.isNull(client) || Objects.isNull(customer)
        || !isClientAndCustomerValid(client, customer)) {
      throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
    }
    ChannelPojo channelPojo = channelApi.getChannelByName(orderForm.getChannelName());
    if (Objects.isNull(channelPojo)) {
      throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL);
    }
    if (Objects.nonNull(orderApi.getOrderByChannelOrderId(orderForm.getChannelOrderId()))) {
      throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
    }
    if (CollectionUtils.isEmpty(orderForm.getOrderItemList())) {
      throw new ApiException(Constants.CODE_EMPTY_ORDER_ITEM_LIST, Constants.MSG_EMPTY_ORDER_ITEM_LIST);
    }
    OrderPojo orderPojo = OrderDtoHelper.createOrderPojo(orderForm.getClientId(), orderForm.getChannelOrderId(), channelPojo,
        orderForm.getCustomerId());
    List<OrderItemPojo> orderItemList = createOrderItemList(orderForm, client, channelPojo, orderPojo);
    orderApi.createOrder(orderPojo, orderItemList);
  }

  public void allocateOrder(Long orderId) {
    orderFlowApi.allocateOrderFlow(orderId);
  }

  public void fulfillOrder(Long orderId, HttpServletResponse response) {
    OrderPojo order = orderApi.getOrderByOrderId(orderId);
    if (Objects.nonNull(order) && order.getStatus() == OrderStatus.ALLOCATED) {
      String fileName = "Invoice_" + order.getChannelOrderId();
      List<OrderItemPojo> orderItems = orderApi.getItemListByOrderId(orderId);
      try {
        byte[] invoiceBytes = invoiceService.generateInvoice(orderItems, order);
        order.setStatus(OrderStatus.FULFILLED);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentLengthLong(invoiceBytes.length);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(invoiceBytes);
        baos.writeTo(response.getOutputStream());
        baos.close();
      }
      catch (Exception e) {
        throw new ApiException(Constants.CODE_ISSUE_GENERATING_INVOICE, Constants.MSG_ISSUE_GENERATING_INVOICE);
      }
      orderApi.fulfillOrder(order);
    } else {
      throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID, "Input order is not allocated");
    }
  }

  private Boolean isClientAndCustomerValid(UserPojo client, UserPojo customer) {
    return client.getType().equals(UserType.CLIENT) && customer.getType().equals(UserType.CUSTOMER);
  }

  private List<OrderItemPojo> convertCsvToPojo(List<OrderUploadCSV> orderUploadDetails) {
    List<OrderItemPojo> orderItems = new ArrayList<>();
    for (OrderUploadCSV orderUploadDetail : orderUploadDetails) {
      ProductPojo productPojo = productApi.getProductByClientSkuID(orderUploadDetail.getClientSkuId());
      if (Objects.isNull(productPojo)) {
        throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
            "ProductPojo with clientSkuId" + orderUploadDetail.getClientSkuId() + "not present");
      }
      OrderItemPojo orderItemPojo = new OrderItemPojo();
      orderItemPojo.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
      orderItemPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
      orderItemPojo.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
      orderItemPojo.setSellingPricePerUnit(orderUploadDetail.getSellingPricePerUnit());
      orderItems.add(orderItemPojo);
    }
    return orderItems;
  }

  private List<OrderItemPojo> createOrderItemList(OrderForm orderForm, UserPojo client, ChannelPojo channelPojo, OrderPojo orderPojo) {
    List<OrderItemPojo> orderItemList = new ArrayList<>();
    for (OrderItemForm item : orderForm.getOrderItemList()) {
      ChannelListingPojo channelListingPojo = channelApi.findByChannelIdAndChannelSkuIdAndClientId(channelPojo.getId(),
          item.getChannelSkuId(), client.getId());
      if (Objects.isNull(channelListingPojo)) {
        throw new ApiException(Constants.CODE_INVALID_CHANNEL_LISTING_ID, Constants.MSG_INVALID_CHANNEL_LISTING_ID,
            "ChannelPojo Listing not found for channelSkuId and clientId: " + item.getChannelSkuId() + ", " + channelPojo.getId());
      }
      OrderItemPojo orderItemPojo = new OrderItemPojo();
      orderItemPojo.setOrderId(orderPojo.getId());
      orderItemPojo.setOrderedQuantity(item.getQuantity());
      orderItemPojo.setSellingPricePerUnit(item.getSellingPricePerUnit());
      orderItemPojo.setGlobalSkuId(channelListingPojo.getGlobalSkuId());
      orderItemList.add(orderItemPojo);
    }
    return orderItemList;
  }
}