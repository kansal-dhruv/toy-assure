package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.*;
import com.increff.ta.model.OrderForm;
import com.increff.ta.model.OrderItemForm;
import com.increff.ta.pojo.OrderItem;
import com.increff.ta.pojo.Orders;
import com.increff.ta.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTestChannel extends AbstractUnitTest{

    @InjectMocks
    OrderService orderService;

    @Mock
    UserDao userDao;

    @Mock
    ChannelDao channelDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderItemDao orderItemDao;

    @Mock
    ChannelListingDao channelListingDao;

    @Test
    public void createOrder(){
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(TestUtils.getChannel());
        Mockito.when(orderDao.findByChannelOrderId(Mockito.anyString()))//
                .thenReturn(null);
        Mockito.when(channelListingDao.findByChannelIdAndChannelSkuidAndClientId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))//
                .thenReturn(TestUtils.getChannelListing());
        Mockito.when(orderItemDao.saveOrUpdate(Mockito.any()))//
                .thenReturn(new OrderItem());
        List<OrderItemForm> orderItemList = new ArrayList<>();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(1L);
        orderItemForm.setChannelSkuId("sku-1");
        orderItemList.add(orderItemForm);

        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId("ch-ordr-1");
        orderForm.setOrderItemList(orderItemList);
        orderForm.setChannelName("FLKRT");
        orderForm.setClientId(1L);
        orderForm.setCustomerId(3L);
        orderService.createOrder(orderForm);
    }

    @Test
    public void createOrderInvalidUser(){
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(null).thenReturn(TestUtils.getCustomerUser());
        List<OrderItemForm> orderItemList = new ArrayList<>();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(1L);
        orderItemForm.setChannelSkuId("sku-1");
        orderItemList.add(orderItemForm);

        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId("ch-ordr-1");
        orderForm.setOrderItemList(orderItemList);
        orderForm.setChannelName("FLKRT");
        orderForm.setClientId(1L);
        orderForm.setCustomerId(3L);
        try {
            orderService.createOrder(orderForm);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
        }
    }

    @Test
    public void createOrderInvalidChannel(){
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(null);
        List<OrderItemForm> orderItemList = new ArrayList<>();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(1L);
        orderItemForm.setChannelSkuId("sku-1");
        orderItemList.add(orderItemForm);

        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId("ch-ordr-1");
        orderForm.setOrderItemList(orderItemList);
        orderForm.setChannelName("FLKRT");
        orderForm.setClientId(1L);
        orderForm.setCustomerId(3L);
        try{
            orderService.createOrder(orderForm);
        } catch (ApiException e){
          Assert.assertEquals(e.getCode(), Constants.CODE_INAVLID_CHANNEL);
        }
    }

    @Test
    public void createOrderDuplicateChannelOrderId(){
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(TestUtils.getChannel());
        Mockito.when(orderDao.findByChannelOrderId(Mockito.anyString()))//
                .thenReturn(new Orders());
        List<OrderItemForm> orderItemList = new ArrayList<>();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(1L);
        orderItemForm.setChannelSkuId("sku-1");
        orderItemList.add(orderItemForm);

        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId("ch-ordr-1");
        orderForm.setOrderItemList(orderItemList);
        orderForm.setChannelName("FLKRT");
        orderForm.setClientId(1L);
        orderForm.setCustomerId(3L);
        try{
            orderService.createOrder(orderForm);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID);
        }
    }

    @Test
    public void createOrderInvalidChannelListing(){
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(TestUtils.getChannel());
        Mockito.when(orderDao.findByChannelOrderId(Mockito.anyString()))//
                .thenReturn(null);
        Mockito.when(channelListingDao.findByChannelIdAndChannelSkuidAndClientId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))//
                .thenReturn(null);
        List<OrderItemForm> orderItemList = new ArrayList<>();
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(1L);
        orderItemForm.setChannelSkuId("sku-1");
        orderItemList.add(orderItemForm);

        OrderForm orderForm = new OrderForm();
        orderForm.setChannelOrderId("ch-ordr-1");
        orderForm.setOrderItemList(orderItemList);
        orderForm.setChannelName("FLKRT");
        orderForm.setClientId(1L);
        orderForm.setCustomerId(3L);
        try{
            orderService.createOrder(orderForm);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_CHANNEL_LISTING_ID);
        }
    }
}
