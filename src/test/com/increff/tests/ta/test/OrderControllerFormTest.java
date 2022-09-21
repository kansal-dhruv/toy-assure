package com.increff.tests.ta.test;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.OrderForm;
import com.increff.ta.commons.model.OrderItemForm;
import com.increff.ta.constants.Constants;
import com.increff.ta.controller.OrderController;
import com.increff.tests.ta.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OrderControllerFormTest extends AbstractUnitTest {

  @Autowired
  private OrderController orderController;

  @Test
  public void Test1_createOrder(){
    OrderForm orderForm = new OrderForm();
    orderForm.setCustomerId(customer.getId());
    orderForm.setClientId(client.getId());
    orderForm.setChannelName("FLIPKART");
    orderForm.setChannelOrderId("order-f-1");

    List<OrderItemForm> orderItemList = new ArrayList<>();
    OrderItemForm orderItemForm = new OrderItemForm();
    orderItemForm.setChannelSkuId("Channel-sku-1");
    orderItemForm.setQuantity(1L);
    orderItemForm.setSellingPricePerUnit(100D);
    orderItemList.add(orderItemForm);

    orderForm.setOrderItemList(orderItemList);
    orderController.orderUpload(orderForm);
  }

  @Test
  public void Test2_createOrderInvalidUser(){
    OrderForm orderForm = new OrderForm();
    orderForm.setCustomerId(client.getId());
    orderForm.setClientId(customer.getId());
    orderForm.setChannelName("FLIPKART");
    orderForm.setChannelOrderId("order-c-1");

    List<OrderItemForm> orderItemList = new ArrayList<>();
    OrderItemForm orderItemForm = new OrderItemForm();
    orderItemForm.setChannelSkuId("Channel-sku-1");
    orderItemForm.setQuantity(1L);
    orderItemForm.setSellingPricePerUnit(100D);
    orderItemList.add(orderItemForm);
    orderForm.setOrderItemList(orderItemList);
    try {
      orderController.orderUpload(orderForm);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
    }
  }

  @Test
  public void Test3_createOrderInvalidChannelOrderId(){
    OrderForm orderForm = new OrderForm();
    orderForm.setCustomerId(customer.getId());
    orderForm.setClientId(client.getId());
    orderForm.setChannelName("FLIPKART");
    orderForm.setChannelOrderId("order-f-1");

    List<OrderItemForm> orderItemList = new ArrayList<>();
    OrderItemForm orderItemForm = new OrderItemForm();
    orderItemForm.setChannelSkuId("Channel-sku-1");
    orderItemForm.setQuantity(1L);
    orderItemForm.setSellingPricePerUnit(100D);
    orderItemList.add(orderItemForm);

    orderForm.setOrderItemList(orderItemList);
    try {
      orderController.orderUpload(orderForm);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID);
    }
  }

  @Test
  public void Test1_createOrderInvalidChannelSku(){
    OrderForm orderForm = new OrderForm();
    orderForm.setCustomerId(customer.getId());
    orderForm.setClientId(client.getId());
    orderForm.setChannelName("FLIPKART");
    orderForm.setChannelOrderId("order-f12-1");

    List<OrderItemForm> orderItemList = new ArrayList<>();
    OrderItemForm orderItemForm = new OrderItemForm();
    orderItemForm.setChannelSkuId("Channel-sku-1231");
    orderItemForm.setQuantity(1L);
    orderItemForm.setSellingPricePerUnit(100D);
    orderItemList.add(orderItemForm);

    orderForm.setOrderItemList(orderItemList);
    try {
      orderController.orderUpload(orderForm);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_CHANNEL_LISTING_ID);
    }
  }

  @Test
  public void Test1_createOrderInvalidChannel(){
    OrderForm orderForm = new OrderForm();
    orderForm.setCustomerId(customer.getId());
    orderForm.setClientId(client.getId());
    orderForm.setChannelName("FLIPKART1234");
    orderForm.setChannelOrderId("order-f-1");
    List<OrderItemForm> orderItemList = new ArrayList<>();
    OrderItemForm orderItemForm = new OrderItemForm();
    orderItemForm.setChannelSkuId("Channel-sku-1");
    orderItemForm.setQuantity(1L);
    orderItemForm.setSellingPricePerUnit(100D);
    orderItemList.add(orderItemForm);
    orderForm.setOrderItemList(orderItemList);
    try {
      orderController.orderUpload(orderForm);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INAVLID_CHANNEL);
    }
  }

  @Test
  public void Test1_createOrderNoOrderItems(){
    OrderForm orderForm = new OrderForm();
    orderForm.setCustomerId(customer.getId());
    orderForm.setClientId(client.getId());
    orderForm.setChannelName("FLIPKART");
    orderForm.setChannelOrderId("order-f1-1");
    try {
      orderController.orderUpload(orderForm);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_EMPTY_ORDER_ITEM_LIST);
    }
  }


}