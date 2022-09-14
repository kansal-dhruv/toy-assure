package com.increff.ta.dto;

import com.increff.ta.api.*;
import com.increff.ta.constants.Constants;
import com.increff.ta.enums.OrderStatus;
import com.increff.ta.enums.UserType;
import com.increff.ta.dto.helper.OrderDtoHelper;
import com.increff.ta.model.OrderForm;
import com.increff.ta.model.OrderItemForm;
import com.increff.ta.model.OrderUploadCSV;
import com.increff.ta.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderDto {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private BinApi binApi;

    @Autowired
    private InvoiceService invoiceService;

    public void createOrderCSV(Long clientId, String channelOrderId, Long customerId, MultipartFile csvFile) {
        List<OrderUploadCSV> orderUploadDetails = OrderDtoHelper.validateAndParseCSV(csvFile);
        User client = userApi.getUserById(clientId);
        User customer = userApi.getUserById(customerId);
        if (client == null || customer == null
            || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
        }
        Channel defaultChannel = channelApi.getChannelByName("INTERNAL");
        if (defaultChannel == null) {
            throw new ApiException(Constants.CODE_INTERNAL_CHANNEL_NOT_FOUND, Constants.MSG_INTERNAL_CHANNEL_NOT_FOUND);
        }
        if (orderApi.getOrderByChannelOrderId(channelOrderId) != null) {
            throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
        }
        Orders orders = OrderDtoHelper.createOrderPojo(clientId, channelOrderId, defaultChannel, customerId);
        List<OrderItem> orderItems = convertCsvToPojo(orderUploadDetails);
        orderApi.createOrder(orders, orderItems);
    }



    public void createOrder(OrderForm orderForm) {
        User client = userApi.getUserById(orderForm.getClientId());
        User customer = userApi.getUserById(orderForm.getCustomerId());
        if (client == null || customer == null
            || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
        }
        Channel channel = channelApi.getChannelByName(orderForm.getChannelName());
        if (channel == null) {
            throw new ApiException(Constants.CODE_INTERNAL_CHANNEL_NOT_FOUND, Constants.MSG_INTERNAL_CHANNEL_NOT_FOUND);
        }
        if (orderApi.getOrderByChannelOrderId(orderForm.getChannelOrderId()) != null) {
            throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
        }
        if (CollectionUtils.isEmpty(orderForm.getOrderItemList())){
            throw new ApiException(Constants.CODE_EMPTY_ORDER_ITEM_LIST, Constants.MSG_EMPTY_ORDER_ITEM_LIST);
        }
        Orders orders = OrderDtoHelper.createOrderPojo(orderForm.getClientId(), orderForm.getChannelOrderId(), channel,
            orderForm.getCustomerId());
        List<OrderItem> orderItemList = createOrderItemList(orderForm, client, channel, orders);
        orderApi.createOrder(orders, orderItemList);
    }

    @Transactional
    public void allocateOrder(Long orderId) {
        Orders order = orderApi.getOrderByOrderId(orderId);
        boolean areAllOrderItemsAllocated = true;
        if(Objects.nonNull(order) && order.getStatus().equals(OrderStatus.CREATED)){
            List<OrderItem> orderItemList = orderApi.getItemListByOrderId(orderId);
            for (OrderItem orderItem : orderItemList) {
                Inventory inv = inventoryService.getInventoryByGlobalSkuId(orderItem.getGlobalSkuId());
                if (inv == null) {
                    throw new ApiException(Constants.CODE_ITEM_NOT_IN_INVENTORY, Constants.MSG_ITEM_NOT_IN_INVENTORY,
                        "Product with globalSkuId: " + orderItem.getGlobalSkuId() + " id not present in inventory");
                }
                areAllOrderItemsAllocated = updateInventoryAndBins(areAllOrderItemsAllocated, orderItem, inv);
            }
            if (areAllOrderItemsAllocated) {
                order.setStatus(OrderStatus.ALLOCATED);
            }
        } else {
            if(Objects.isNull(order))
                throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID,
                    "Order Id is not valid");
            else
                throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID,
                    "Order status is not created");
        }
    }

    public void fulfillOrder(Long orderId, HttpServletResponse response) {
        Orders order = orderApi.getOrderByOrderId(orderId);
        if (order!=null && order.getStatus() == OrderStatus.ALLOCATED) {
            String fileName = "Invoice_" + order.getChannelOrderId();
            List<OrderItem> orderItems = orderApi.getItemListByOrderId(orderId);
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
            } catch (Exception e){
                throw new ApiException(Constants.CODE_ISSUE_GENERATING_INVOICE, Constants.MSG_ISSUE_GENERATING_INVOICE);
            }
            orderApi.fulfillOrder(order);
        } else {
            throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID, "Input order is not allocated");
        }
    }

    private Boolean isClientAndCustomerValid(User client, User customer) {
        return client.getType().equals(UserType.CLIENT) && customer.getType().equals(UserType.CUSTOMER);
    }

    private List<OrderItem> convertCsvToPojo(List<OrderUploadCSV> orderUploadDetails){
        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderUploadCSV orderUploadDetail : orderUploadDetails){
            Product product = productApi.getProductByClientSkuID(orderUploadDetail.getClientSkuId());
            if(product == null){
                throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
                    "Product with clientSkuId" + orderUploadDetail.getClientSkuId() + "not present");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
            orderItem.setGlobalSkuId(product.getGlobalSkuId());
            orderItem.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
            orderItem.setSellingPricePerUnit(orderUploadDetail.getSellingPricePerUnit());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private List<OrderItem> createOrderItemList(OrderForm orderForm, User client, Channel channel, Orders orders) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemForm item : orderForm.getOrderItemList()) {
            ChannelListing channelListing = channelApi.findByChannelIdAndChannelSkuidAndClientId(client.getId(),
                item.getChannelSkuId(), channel.getId());
            if (channelListing == null) {
                throw new ApiException(Constants.CODE_INVALID_CHANNEL_LISTING_ID, Constants.MSG_INVALID_CHANNEL_LISTING_ID,
                    "Channel Listing not found for channelSkuId and clientId: " + item.getChannelSkuId() + ", " + channel.getId());
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orders.getId());
            orderItem.setOrderedQuantity(item.getQuantity());
            orderItem.setGlobalSkuId(channelListing.getGlobalSkuId());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private boolean updateInventoryAndBins(boolean areAllOrderItemsAllocated, OrderItem orderItem, Inventory inv) {
        Long allocationAmount = Math.min(inv.getAvailableQuantity(), orderItem.getOrderedQuantity() - orderItem.getAllocatedQuanity());
        inv.setAvailableQuantity(inv.getAvailableQuantity() - allocationAmount);
        inv.setAllocatedQuantity(inv.getAllocatedQuantity() + allocationAmount);
        orderItem.setAllocatedQuanity(orderItem.getAllocatedQuanity() + allocationAmount);
        if (!Objects.equals(orderItem.getAllocatedQuanity(), orderItem.getOrderedQuantity())) {
            areAllOrderItemsAllocated = false;
        }
        List<BinSku> binSkuList = binApi.getBinsWithProduct(orderItem.getGlobalSkuId());
        int binCount = 0;
        while (allocationAmount != 0) {
            BinSku selectedBin = binSkuList.get(binCount);
            Long availAmount = Math.min(allocationAmount, selectedBin.getQuantity());
            allocationAmount = allocationAmount - availAmount;
            selectedBin.setQuantity(selectedBin.getQuantity() - availAmount);
            binCount++;
        }
        return areAllOrderItemsAllocated;
    }
}