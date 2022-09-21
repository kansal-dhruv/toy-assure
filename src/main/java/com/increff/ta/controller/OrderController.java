package com.increff.ta.controller;

import com.increff.ta.commons.model.OrderForm;
import com.increff.ta.dto.OrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final OrderDto orderDto;

  public OrderController(OrderDto orderDto) {
    this.orderDto = orderDto;
  }

  @ApiOperation("This API is used to create orders from UI")
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public void orderUpload(@RequestParam("clientId") @NotNull(message = "Client Id cannot by null") Long clientId,
                                            @RequestParam("channelOrderId") @NotBlank(message = "ChannelPojo Order ID cannot by blank") String channelOrderId,
                                            @RequestParam("customerId") @NotNull(message = "Customer ID cannot be null") Long customerId,
                                            @RequestPart("csvFile") @NotNull(message = "CSV File cannot be null") MultipartFile csvFile) {
    orderDto.createOrderCSV(clientId, channelOrderId, customerId, csvFile);
  }

  @ApiOperation("This API is used to create order from other Channels")
  @RequestMapping(value = "/channel", method = RequestMethod.POST)
  public void orderUpload(@RequestBody @Valid OrderForm orderForm) {
    orderDto.createOrder(orderForm);
  }

  @ApiOperation("This API is used to allocate quantity to orders for CREATED orders")
  @RequestMapping(value = "/allocate", method = RequestMethod.POST)
  public void allocateOrder(@RequestParam("orderId") Long orderId) {
    orderDto.allocateOrder(orderId);
  }

  @ApiOperation("This API is used to fulfill orders for ALLOCATED orders")
  @RequestMapping(value = "/fulfill", method = RequestMethod.POST)
  public void fulfillOrder(@RequestParam("orderId") Long orderId,
                           HttpServletResponse response) {
    orderDto.fulfillOrder(orderId, response);
  }
}