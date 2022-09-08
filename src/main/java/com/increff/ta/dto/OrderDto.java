package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.OrderApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.model.OrderForm;
import com.increff.ta.model.OrderUploadCSV;
import com.increff.ta.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderDto {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductDao productDao;


    public void createOrderCSV(Long clientId, String channelOrderId, Long customerId, MultipartFile csvFile) {
        List<OrderUploadCSV> orderUploadDetails = null;
        try {
            orderUploadDetails = CSVUtils.parseCSV(csvFile.getBytes(), OrderUploadCSV.class);
        } catch (IOException e) {
            throw new ApiException(Constants.CODE_ERROR_PARSING_CSV_FILE, Constants.MSG_ERROR_PARSING_CSV_FILE);
        }
        Set<String> clientSkuIds = orderUploadDetails.stream().map((OrderUploadCSV orderUploadDetail) -> orderUploadDetail.getClientSkuId()).collect(Collectors.toSet());
        if (clientSkuIds.size() != orderUploadDetails.size()) {
            throw new ApiException(Constants.CODE_DUPLICATE_CLIENT_SKU_ID, Constants.MSG_DUPLICATE_CLIENT_SKU_ID);
        }
        orderApi.createOrderCSV(clientId, channelOrderId, customerId, orderUploadDetails);

    }

    public void createOrder(OrderForm orderForm) {
        orderApi.createOrder(orderForm);
    }

    public void allocateOrder(Long orderId) {
        if(orderId < 0){
            throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID, "Order Id cannot be negative");
        }
        orderApi.allocateOrder(orderId);
    }

    public void fulfillOrder(Long orderId, HttpServletResponse response) {
        if(orderId < 0){
            throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID, "Order Id cannot be negative");
        }
        orderApi.fulfillOrder(orderId, response);
    }
}