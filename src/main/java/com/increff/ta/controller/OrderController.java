package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.dto.OrderDto;
import com.increff.ta.model.OrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Api
@RestController
@Validated
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation("This API is used to create orders from UI")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> orderUpload(@RequestParam("clientId") @NotNull(message = "Client Id cannot by null") Long clientId,
                                              @RequestParam("channelOrderId") @NotBlank(message = "Channel Order ID cannot by blank") String channelOrderId,
                                              @RequestParam("customerId") @NotNull(message = "Customer ID cannot be null") Long customerId,
                                              @RequestPart("csvFile") @NotNull(message = "CSV File cannot be null") MultipartFile csvFile) {
        orderDto.createOrderCSV(clientId, channelOrderId, customerId, csvFile);
        return ResponseHandler.successResponse();
    }

    @ApiOperation("This API is used to create order from other Channels")
    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public ResponseEntity<Object> orderUpload(@RequestBody @Valid OrderForm orderForm) {
        orderDto.createOrder(orderForm);
        return ResponseHandler.successResponse();
    }

    @ApiOperation("This API is used to allocate quantity to orders for CREATED orders")
    @RequestMapping(value = "/allocate", method = RequestMethod.POST)
    public ResponseEntity<Object> allocateOrder(@RequestParam("orderId") Long orderId) {
        orderDto.allocateOrder(orderId);
        return ResponseHandler.successResponse();
    }

    @ApiOperation("This API is used to fulfill orders for ALLOCATED orders")
    @RequestMapping(value = "/fulfill", method = RequestMethod.POST)
    public void fulfillOrder(@RequestParam("orderId") Long orderId,
                             HttpServletResponse response) {
        orderDto.fulfillOrder(orderId, response);
    }
}