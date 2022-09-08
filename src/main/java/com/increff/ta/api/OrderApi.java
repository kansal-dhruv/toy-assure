package com.increff.ta.api;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.*;
import com.increff.ta.enums.OrderStatus;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.OrderForm;
import com.increff.ta.model.OrderItemForm;
import com.increff.ta.model.OrderUploadCSV;
import com.increff.ta.pojo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderApi {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ChannelListingDao channelListingDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private InvoiceService invoiceService;

    @Transactional
    public void createOrderCSV(Long clientId, String channelOrderId,
                               Long customerId, List<OrderUploadCSV> orderUploadDetails) {
        User client = userDao.selectById(clientId);
        User customer = userDao.selectById(customerId);
        if (client == null || customer == null
                || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
        }

        Channel defaultChannel = channelDao.findByChannelName("INTERNAL");
        if (defaultChannel == null) {
            throw new ApiException(Constants.CODE_INTERNAL_CHANNEL_NOT_FOUND, Constants.MSG_INTERNAL_CHANNEL_NOT_FOUND);
        }
        if (orderDao.findByChannelOrderId(channelOrderId) != null) {
            throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
        }

        Orders orders = new Orders();
        orders.setClientId(clientId);
        orders.setChannelOrderId(channelOrderId);
        orders.setChannelId(defaultChannel.getId());
        orders.setCustomerId(customerId);
        orders.setStatus(OrderStatus.CREATED);
        orders = orderDao.saveOrUpdate(orders);

        List<OrderItem> orderItems = convertCsvToPojo(orderUploadDetails);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(orders.getId());
            orderItemDao.saveOrUpdate(orderItem);
        }
    }

    @Transactional
    public void createOrder(OrderForm orderForm) {
        User client = userDao.selectById(orderForm.getClientId());
        User customer = userDao.selectById(orderForm.getCustomerId());
        if (client == null || customer == null
                || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
        }
        Channel channel = channelDao.findByChannelName(orderForm.getChannelName());
        if (channel == null) {
            throw new ApiException(Constants.CODE_INAVLID_CHANNEL, Constants.MSG_INVALID_CHANNEL,
                    "Channel: " + orderForm.getChannelName() + " is not present");
        }
        if (orderDao.findByChannelOrderId(orderForm.getChannelOrderId()) != null) {
            throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
        }
        if (CollectionUtils.isEmpty(orderForm.getOrderItemList())){
            throw new ApiException(Constants.CODE_EMPTY_ORDER_ITEM_LIST, Constants.MSG_EMPTY_ORDER_ITEM_LIST);
        }
        Orders orders = new Orders();
        orders.setClientId(orders.getClientId());
        orders.setChannelOrderId(orderForm.getChannelOrderId());
        orders.setChannelId(channel.getId());
        orders.setCustomerId(orderForm.getCustomerId());
        orders.setStatus(OrderStatus.CREATED);
        orders = orderDao.saveOrUpdate(orders);

        for (OrderItemForm item : orderForm.getOrderItemList()) {
            ChannelListing channelListing = channelListingDao.findByChannelIdAndChannelSkuidAndClientId(client.getId(), channel.getId(), item.getChannelSkuId());
            if (channelListing == null)
                throw new ApiException(Constants.CODE_INVALID_CHANNEL_LISTING_ID, Constants.MSG_INVALID_CHANNEL_LISTING_ID,
                        "Channel Listing not found for channelSkuId and clientId: " + item.getChannelSkuId() + ", " + channel.getId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orders.getId());
            orderItem.setOrderedQuantity(item.getQuantity());
            orderItem.setGlobalSkuId(channelListing.getGlobalSkuId());
            orderItemDao.saveOrUpdate(orderItem);
        }
    }

    @Transactional
    public void allocateOrder(Long orderId) {
        Orders order = orderDao.findByOrderId(orderId);
        boolean areAllOrderItemsAllocated = true;
        if (order!=null && order.getStatus().equals(OrderStatus.CREATED)) {
            List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
            for (OrderItem orderItem : orderItemList) {
                Inventory inv = inventoryDao.findByGlobalSkuid(orderItem.getGlobalSkuId());
                if (inv == null) {
                    throw new ApiException(Constants.CODE_ITEM_NOT_IN_INVENTORY, Constants.MSG_ITEM_NOT_IN_INVENTORY,
                            "Product with globalSkuId: " + orderItem.getGlobalSkuId() + " id not present in inventory");

                }
                Long allocationAmount = Math.min(inv.getAvailableQuantity(), orderItem.getOrderedQuantity() - orderItem.getAllocatedQuanity());
                inv.setAvailableQuantity(inv.getAvailableQuantity() - allocationAmount);
                inv.setAllocatedQuantity(inv.getAllocatedQuantity() + allocationAmount);
                orderItem.setAllocatedQuanity(orderItem.getAllocatedQuanity() + allocationAmount);
                if (!Objects.equals(orderItem.getAllocatedQuanity(), orderItem.getOrderedQuantity())) {
                    areAllOrderItemsAllocated = false;
                }
                List<BinSku> binSkuList = binSkuDao.findByglobalSkuId(orderItem.getGlobalSkuId());
                int binCount = 0;
                while (allocationAmount != 0) {
                    BinSku selectedBin = binSkuList.get(binCount);
                    Long availAmount = Math.min(allocationAmount, selectedBin.getQuantity());
                    allocationAmount = allocationAmount - availAmount;
                    selectedBin.setQuantity(selectedBin.getQuantity() - availAmount);
                    binCount++;
                    binSkuDao.insertOrUpdate(selectedBin);
                }
                orderItemDao.saveOrUpdate(orderItem);
                inventoryDao.insertOrUpdate(inv);
            }
            if (areAllOrderItemsAllocated) {
                order.setStatus(OrderStatus.ALLOCATED);
                orderDao.saveOrUpdate(order);
            }
        } else {
            throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID,
                    "Provided OrderID is already " + order.getStatus().name());
        }
    }

    @Transactional
    public void fulfillOrder(Long orderId, HttpServletResponse response) {
        Orders order = orderDao.findByOrderId(orderId);
        if (order!=null && order.getStatus() == OrderStatus.ALLOCATED) {
            String fileName = "Invoice_" + orderId;
            List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
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
            Product prodcut = productDao.findByClientSkuId(orderUploadDetail.getClientSkuId());
            if(prodcut == null){
                throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
                        "Product with clientSkuId" + orderUploadDetail.getClientSkuId() + "not present");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
            orderItem.setGlobalSkuId(prodcut.getGlobalSkuId());
            orderItem.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
            orderItem.setSellingPricePerUnit(orderUploadDetail.getSellingPricePerUnit());
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
