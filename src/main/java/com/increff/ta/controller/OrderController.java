package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.model.OrderForm;
import com.increff.ta.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.net.URISyntaxException;

@Api
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Transactional
    @ApiOperation("This API is used to create orders from UI")
    @RequestMapping(value = "/api/order/create", method = RequestMethod.POST)
    public ResponseEntity<Object> orderUpload(@RequestParam("clientId") Long clientId,
                                              @RequestParam("channelOrderId") String channelOrderId,
                                              @RequestParam("customerId") Long customerId,
                                              @RequestParam("csvFile") MultipartFile csvFile) {
        orderService.createOrderCSV(clientId, channelOrderId, customerId, csvFile);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @ApiOperation("This API is used to create order from other Channels")
    @RequestMapping(value = "/api/order/channel/create", method = RequestMethod.POST)
    public ResponseEntity<Object> orderUpload(@RequestBody OrderForm orderForm){
        orderService.createOrder(orderForm);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @ApiOperation("This API is used to allocate quantity to orders for CREATED orders")
    @RequestMapping(value = "/api/order/allocate", method = RequestMethod.POST)
    public ResponseEntity<Object> allocateOrder(@RequestParam("orderId") Long orderId){
        orderService.allocateOrder(orderId);
        return ResponseHandler.successResponse();
    }

    @Transactional
    @RequestMapping(value = "/api/order/fulfill", method = RequestMethod.POST)
    public ResponseEntity<Object> fulfillOrder(@RequestParam("orderId") Long orderId) throws JAXBException, URISyntaxException {
        orderService.fulfillOrder(orderId);
        return ResponseHandler.successResponse();
    }
}
