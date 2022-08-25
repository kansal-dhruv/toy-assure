package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.*;
import com.increff.ta.pojo.OrderItem;
import com.increff.ta.pojo.Orders;
import com.increff.ta.utils.FileUtils;
import com.increff.ta.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTestCSV extends AbstractUnitTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    UserDao userDao;

    @Mock
    ChannelDao channelDao;

    @Mock
    OrderDao orderDao;

    @Mock
    ProductDao productDao;

    @Mock
    OrderItemDao orderItemDao;

    @Test
    public void createOrder() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(TestUtils.getInternalChannel());
        Mockito.when(orderDao.findByChannelOrderId(Mockito.anyString()))//
                .thenReturn(null);
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))//
                .thenReturn(TestUtils.getProduct());
        Mockito.when(orderItemDao.saveOrUpdate(Mockito.any()))//
                .thenReturn(new OrderItem());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("createOrderInternal.csv");
        orderService.createOrderCSV(1L, "ORDER-INTRNL-1",2L,csvFile);
    }

    @Test
    public void createOrderInvalidUser() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(null).thenReturn(TestUtils.getCustomerUser());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("createOrderInternal.csv");
        try {
            orderService.createOrderCSV(1L, "ORDER-INTRNL-1", 2L, csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
            return;
        }
        Assert.fail();
    }

    @Test
    public void createOrderInvalidCSV() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("invalidCSV.html");
        try {
            orderService.createOrderCSV(1L, "ORDER-INTRNL-1", 2L, csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
            return;
        }
        Assert.fail();
    }

    @Test
    public void createOrderDuplicateClientSKU() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("createOrderInternal-duplicateClientSku.csv");
        try {
            orderService.createOrderCSV(1L, "ORDER-INTRNL-1", 2L, csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CLIENT_SKU_ID);
            return;
        }
        Assert.fail();
    }

    @Test
    public void createOrderDuplicateChannelOrderId() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(TestUtils.getInternalChannel());
        Mockito.when(orderDao.findByChannelOrderId(Mockito.anyString()))//
                .thenReturn(new Orders());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("createOrderInternal.csv");
        try {
            orderService.createOrderCSV(1L, "ORDER-INTRNL-1", 2L, csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID);
        }
    }

    @Test
    public void createOrderNoInternalChannel() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(null);
        MockMultipartFile csvFile = FileUtils.getFileFromResources("createOrderInternal.csv");
        try {
            orderService.createOrderCSV(1L, "ORDER-INTRNL-1", 2L, csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INTERNAL_CHANNEL_NOT_FOUND);
        }
    }

    @Test
    public void createOrderInvalidProduct() {
        Mockito.when(userDao.selectById(Mockito.anyLong()))//
                .thenReturn(TestUtils.getClientUser()).thenReturn(TestUtils.getCustomerUser());
        Mockito.when(channelDao.findByChannelName(Mockito.anyString()))//
                .thenReturn(TestUtils.getInternalChannel());
        Mockito.when(orderDao.findByChannelOrderId(Mockito.anyString()))//
                .thenReturn(null);
        Mockito.when(productDao.findByClientSkuId(Mockito.anyString()))//
                .thenReturn(null);
        Mockito.when(orderItemDao.saveOrUpdate(Mockito.any()))//
                .thenReturn(new OrderItem());
        MockMultipartFile csvFile = FileUtils.getFileFromResources("createOrderInternal.csv");
        try {
            orderService.createOrderCSV(1L, "ORDER-INTRNL-1", 2L, csvFile);
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
        }
    }
}