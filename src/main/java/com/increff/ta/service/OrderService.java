package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.*;
import com.increff.ta.enums.OrderStatus;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.OrderForm;
import com.increff.ta.model.OrderItemForm;
import com.increff.ta.model.OrderUploadCSV;
import com.increff.ta.pojo.*;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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

    @Autowired
    private InvoiceService invoiceService;

    public void createOrderCSV(Long clientId, String channelOrderId,
                               Long customerId, MultipartFile csvFile) {
        User client = userDao.selectById(clientId);
        User customer = userDao.selectById(customerId);
        if (client == null || customer == null
                || !isClientAndCustomerValid(client, customer)) {
            throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER);
        }
        List<OrderUploadCSV> orderUploadDetails = null;
        try {
            orderUploadDetails = new CsvToBeanBuilder(new InputStreamReader(new ByteArrayInputStream(csvFile.getBytes()), "UTF8"))
                    .withType(OrderUploadCSV.class).withSkipLines(1).build().parse();
        } catch (IOException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        Set<String> clientSkuIds = orderUploadDetails.stream().map((OrderUploadCSV orderUploadDetail) -> orderUploadDetail.getClientSkuId()).collect(Collectors.toSet());
        if (clientSkuIds.size() != orderUploadDetails.size()) {
            throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
        }
        Channel defaultChannel = channelDao.findByChannelName("INTERNAL");
        if (defaultChannel == null) {
            throw new ApiException(Constants.CODE_INTERNAL_CHANNEL_NOT_FOUND, Constants.MSG_INTERNAL_CHANNEL_NOT_FOUND);
        }
        if (orderDao.findByChannelOrderId(channelOrderId) != null) {
            throw new ApiException(Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID, Constants.MSG_DUPLICATE_CHANNEL_ORDER_ID);
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
                throw new ApiException(Constants.CODE_PRODUCT_NOT_FOUND, Constants.MSG_PRODUCT_NOT_FOUND,
                        "Product with clientSkuId" + orderUploadDetail.getClientSkuId() + "not present");
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
        Orders orders = new Orders();
        orders.setClient(client);
        orders.setChannelOrderId(orderForm.getChannelOrderId());
        orders.setChannel(channel);
        orders.setCustomer(customer);
        orders.setStatus(OrderStatus.CREATED);
        orders = orderDao.saveOrUpdate(orders);

        for (OrderItemForm item : orderForm.getOrderItemList()) {
            ChannelListing channelListing = channelListingDao.findByChannelIdAndChannelSkuidAndClientId(client.getId(), channel.getId(), item.getChannelSkuId());
            if (channelListing == null)
                throw new ApiException("Channel Listing not found for channelSkuId and clientId: " + item.getChannelSkuId() + ", " + channel.getId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setOrderedQuantity(item.getQuantity());
            orderItem.setProduct(channelListing.getProduct());
            orderItemDao.saveOrUpdate(orderItem);
        }
    }

    public void allocateOrder(Long orderId) {
        Orders order = orderDao.findByOrderId(orderId);
        boolean areAllOrderItemsAllocated = true;
        if (order.getStatus().equals(OrderStatus.CREATED)) {
            List<OrderItem> orderItemList = orderItemDao.findByOrderId(orderId);
            for (OrderItem orderItem : orderItemList) {
                Inventory inv = inventoryDao.findByGlobalSkuid(orderItem.getProduct().getGlobalSkuId());
                if (inv == null) {
                    throw new ApiException(Constants.CODE_ITEM_NOT_IN_INVENTORY, Constants.MSG_ITEM_NOT_IN_INVENTORY,
                            "Product with clientSkuID: " + orderItem.getProduct().getClientSkuId() + " id not present in inventory");

                }
                Long allocationAmount = Math.min(inv.getAvailableQuantity(), orderItem.getOrderedQuantity() - orderItem.getAllocatedQuanity());
                inv.setAvailableQuantity(inv.getAvailableQuantity() - allocationAmount);
                inv.setAllocatedQuantity(inv.getAllocatedQuantity() + allocationAmount);
                orderItem.setAllocatedQuanity(orderItem.getAllocatedQuanity() + allocationAmount);
                if (!Objects.equals(orderItem.getAllocatedQuanity(), orderItem.getOrderedQuantity())) {
                    areAllOrderItemsAllocated = false;
                }
                List<BinSku> binSkuList = binSkuDao.findByglobalSkuId(orderItem.getProduct().getGlobalSkuId());
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

    private Boolean isClientAndCustomerValid(User client, User customer) {
        return client.getType().equals(UserType.CLIENT) && customer.getType().equals(UserType.CUSTOMER);
    }

    public void fulfillOrder(Long orderId, HttpServletResponse response) throws JAXBException, URISyntaxException, IOException {
        Orders order = orderDao.findByOrderId(orderId);
        if (order.getStatus() == OrderStatus.ALLOCATED) {
            List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
            byte[] invoiceBytes = invoiceService.generateInvoice(orderItems, order);
            order.setStatus(OrderStatus.FULFILLED);
            response.setContentType("application/pdf");
            response.setContentLengthLong(invoiceBytes.length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(invoiceBytes);
            baos.writeTo(response.getOutputStream());
            baos.close();
        } else {
            throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID, "Input order is not allocated");
        }
    }
}
