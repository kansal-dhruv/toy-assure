package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.model.OrderForm;
import com.increff.ta.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

@Api
@RestController
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Transactional
    @ApiOperation("This API is used to create orders from UI")
    @RequestMapping(value = "/api/order/create", method = RequestMethod.POST)
    public ResponseEntity<Object> orderUpload(@RequestParam("clientId") @NotNull(message = "Client Id cannot by null") Long clientId,
                                              @RequestParam("channelOrderId") @NotBlank(message = "Channel Order ID cannot by blank") String channelOrderId,
                                              @RequestParam("customerId") @NotNull(message = "Customer ID cannot be null") Long customerId,
                                              @RequestParam("csvFile") @NotNull(message = "CSV File cannot be null") MultipartFile csvFile) {
        orderService.createOrderCSV(clientId, channelOrderId, customerId, csvFile);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @ApiOperation("This API is used to create order from other Channels")
    @RequestMapping(value = "/api/order/channel/create", method = RequestMethod.POST)
    public ResponseEntity<Object> orderUpload(@RequestBody OrderForm orderForm) {
        orderService.createOrder(orderForm);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @ApiOperation("This API is used to allocate quantity to orders for CREATED orders")
    @RequestMapping(value = "/api/order/allocate", method = RequestMethod.POST)
    public ResponseEntity<Object> allocateOrder(@RequestParam("orderId") Long orderId) {
        orderService.allocateOrder(orderId);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @RequestMapping(value = "/api/order/fulfill", method = RequestMethod.POST)
    public void fulfillOrder(@RequestParam("orderId") Long orderId, HttpServletResponse response) throws JAXBException, URISyntaxException, IOException {
        orderService.fulfillOrder(orderId, response);
    }
}
