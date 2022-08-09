package com.increff.ta.service;

import com.increff.ta.dao.*;
import com.increff.ta.enums.OrderStatus;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.OrderForm;
import com.increff.ta.model.OrderItemForm;
import com.increff.ta.model.OrderUploadCSV;
import com.increff.ta.pojo.*;
import com.opencsv.bean.CsvToBeanBuilder;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

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

    public void createOrderCSV(Long clientId, String channelOrderId,
                            Long customerId, MultipartFile csvFile) {
        User client = userDao.selectById(clientId);
        User customer = userDao.selectById(customerId);
        if (client == null || customer == null
                || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException("Invalid customer or client Id");
        }
        List<OrderUploadCSV> orderUploadDetails = null;
        try {
            orderUploadDetails = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvFile.getBytes()), "UTF8"))
                    .withType(OrderUploadCSV.class).withSkipLines(1).build().parse();
        } catch (IOException e) {
            throw new ApiException("Unable to parse CSV File");
        }
        Set<String> clientSkuIds = orderUploadDetails.stream().map((OrderUploadCSV orderUploadDetail) -> orderUploadDetail.getClientSkuId()).collect(Collectors.toSet());
        if (clientSkuIds.size() != orderUploadDetails.size()) {
            throw new ApiException("Duplicate clientSkuId in CSV");
        }
        Channel defaultChannel = channelDao.findByChannelName("INTERNAL");
        if (defaultChannel == null) {
            throw new ApiException("Internal channel not found");
        }
        if (orderDao.findByChannelOrderId(channelOrderId) != null) {
            throw new ApiException("Given channel orders id already preset");
        }

        Orders orders = new Orders();
        orders.setClient(client);
        orders.setChannelOrderId(channelOrderId);
        orders.setChannel(defaultChannel);
        orders.setCustomer(customer);
        orders.setStatus(OrderStatus.CREATED);
        orders = orderDao.saveOrUpdate(orders);

        for (OrderUploadCSV orderUploadDetail : orderUploadDetails) {
            Product product = productDao.findByClientSkuId(orderUploadDetail.getClientSkuId());
            if (product == null) {
                throw new ApiException("Product with clientSkuId" + orderUploadDetail.getClientSkuId() + "not present");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setOrderedQuantity(orderUploadDetail.getOrderedQuantity());
            orderItem.setProduct(product);
            orderItem.setSellingPricePerUnit(orderUploadDetail.getSellingPricePerUnit());
            orderItemDao.saveOrUpdate(orderItem);
        }
    }

    public void createOrder(OrderForm orderForm) {
        User client = userDao.selectById(orderForm.getClientId());
        User customer = userDao.selectById(orderForm.getCustomerId());
        if (client == null || customer == null
                || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException("Invalid customer or client Id");
        }
        Channel channel = channelDao.findByChannelName(orderForm.getChannelName());
        if (channel == null) {
            throw new ApiException("Provided channel not found");
        }
        if (orderDao.findByChannelOrderId(orderForm.getChannelOrderId()) != null) {
            throw new ApiException("Given channel orders id already preset");
        }
        Orders orders = new Orders();
        orders.setClient(client);
        orders.setChannelOrderId(orderForm.getChannelOrderId());
        orders.setChannel(channel);
        orders.setCustomer(customer);
        orders.setStatus(OrderStatus.CREATED);
        orders = orderDao.saveOrUpdate(orders);

        for(OrderItemForm item:orderForm.getOrderItemList()){
            ChannelListing channelListing = channelListingDao.findByChannelIdAndChannelSkuidAndClientId(client.getId(), channel.getId(), item.getChannelSkuId());
            if( channelListing == null) throw new ApiException("Channel Listing not found for channelSkuId and clientId: " + item.getChannelSkuId() + ", " + channel.getId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setOrderedQuantity(item.getQuantity());
            orderItem.setProduct(channelListing.getProduct());
            orderItemDao.saveOrUpdate(orderItem);
        }
    }

    public void allocateOrder(Long orderId){
        Orders order = orderDao.findByOrderId(orderId);
        boolean areAllOrderItemsAllocated = true;
        if(order.getStatus().equals(OrderStatus.CREATED)) {
            List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
            for (OrderItem orderItem : orderItemList) {
                Inventory inv = inventoryDao.findByGlobalSkuid(orderItem.getProduct().getGlobalSkuId());
                Long allocationAmount = Math.min(inv.getAvailableQuantity(), orderItem.getOrderedQuantity() - orderItem.getAllocatedQuanity());
                inv.setAvailableQuantity(inv.getAvailableQuantity() - allocationAmount);
                inv.setAllocatedQuantity(inv.getAllocatedQuantity() + allocationAmount);
                orderItem.setAllocatedQuanity(orderItem.getAllocatedQuanity() + allocationAmount);
                if(!Objects.equals(orderItem.getAllocatedQuanity(), orderItem.getOrderedQuantity())){
                    areAllOrderItemsAllocated = false;
                }
                List<BinSku> binSkuList = binSkuDao.findByglobalSkuId(orderItem.getProduct().getGlobalSkuId());
                int binCount = 0;
                while(allocationAmount != 0){
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
            if(areAllOrderItemsAllocated){
                order.setStatus(OrderStatus.ALLOCATED);
                orderDao.saveOrUpdate(order);
            }
        } else {
            throw new ApiException("Provided OrderID is already " + order.getStatus().name());
        }
    }

    private Boolean isClientAndCustomerValid(User client, User customer) {
        return client.getType().equals(UserType.CLIENT) && customer.getType().equals(UserType.CUSTOMER);
    }
}
