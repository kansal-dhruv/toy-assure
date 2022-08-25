package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.BinSkuDao;
import com.increff.ta.dao.InventoryDao;
import com.increff.ta.dao.OrderDao;
import com.increff.ta.dao.OrderItemDao;
import com.increff.ta.pojo.BinSku;
import com.increff.ta.pojo.Inventory;
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
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderItemDao orderItemDao;

    @Mock
    InventoryDao inventoryDao;

    @Mock
    BinSkuDao binSkuDao;

    @Mock
    InvoiceService invoiceService;

    @Test
    public void allocateOrderInsufficientInventory(){
        Mockito.when(orderDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getCreatedOrder());
        Mockito.when(orderItemDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getOrderItems());
        Mockito.when(inventoryDao.findByGlobalSkuid(1L)).thenReturn(TestUtils.getInventory());
        Mockito.when(inventoryDao.findByGlobalSkuid(2L)).thenReturn(TestUtils.getInventory2());
        Mockito.when(binSkuDao.findByglobalSkuId(1L)).thenReturn(Arrays.asList(TestUtils.getBinSku()));
        Mockito.when(binSkuDao.findByglobalSkuId(2L)).thenReturn(Arrays.asList(TestUtils.getBinSku()));
        Mockito.when(binSkuDao.insertOrUpdate(Mockito.any())).thenReturn(new BinSku());
        Mockito.when(orderItemDao.saveOrUpdate(Mockito.any())).thenReturn(new OrderItem());
        Mockito.when(inventoryDao.insertOrUpdate(Mockito.any())).thenReturn(new Inventory());
        Mockito.when(orderDao.saveOrUpdate(Mockito.any())).thenReturn(new Orders());
        orderService.allocateOrder(TestUtils.getCreatedOrder().getId());
    }

    @Test
    public void allocateOrder(){
        Mockito.when(orderDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getCreatedOrder());
        Mockito.when(orderItemDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getOrderItems());
        Mockito.when(inventoryDao.findByGlobalSkuid(1L)).thenReturn(TestUtils.getInventory());
        Mockito.when(inventoryDao.findByGlobalSkuid(2L)).thenReturn(TestUtils.getInventory2());
        Mockito.when(binSkuDao.findByglobalSkuId(1L)).thenReturn(Arrays.asList(TestUtils.getBinSku()));
        Mockito.when(binSkuDao.findByglobalSkuId(2L)).thenReturn(TestUtils.getBinSkuListProduct2());
        Mockito.when(binSkuDao.insertOrUpdate(Mockito.any())).thenReturn(new BinSku());
        Mockito.when(orderItemDao.saveOrUpdate(Mockito.any())).thenReturn(new OrderItem());
        Mockito.when(inventoryDao.insertOrUpdate(Mockito.any())).thenReturn(new Inventory());
        Mockito.when(orderDao.saveOrUpdate(Mockito.any())).thenReturn(new Orders());
        orderService.allocateOrder(TestUtils.getCreatedOrder().getId());
    }

    @Test
    public void allocateOrderInvalidProduct(){
        Mockito.when(orderDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getCreatedOrder());
        Mockito.when(orderItemDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getOrderItems());
        Mockito.when(inventoryDao.findByGlobalSkuid(1L)).thenReturn(null);
        try {
            orderService.allocateOrder(TestUtils.getCreatedOrder().getId());
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_ITEM_NOT_IN_INVENTORY);
        }
    }

    @Test
    public void allocateOrderInvalidOrder(){
        Mockito.when(orderDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getAllocatedOrder());
        try {
            orderService.allocateOrder(TestUtils.getAllocatedOrder().getId());
        } catch (ApiException e){
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_ORDER_ID);
        }
    }

    @Test
    public void fulfillOrder(){
        HttpServletResponse response = new MockHttpServletResponse();
        Mockito.when(orderDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getAllocatedOrder());
        Mockito.when(orderItemDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getOrderItems());
        try {
            Mockito.when(invoiceService.generateInvoice(Mockito.any(), Mockito.any())).thenReturn(new byte[6]);
            orderService.fulfillOrder(TestUtils.getAllocatedOrder().getId(), response);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void fulfillOrderInvalidOrder(){
        HttpServletResponse response = new MockHttpServletResponse();
        Mockito.when(orderDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getCreatedOrder());
        Mockito.when(orderItemDao.findByOrderId(Mockito.anyLong())).thenReturn(TestUtils.getOrderItems());
        try {
            Mockito.when(invoiceService.generateInvoice(Mockito.any(), Mockito.any())).thenReturn(new byte[6]);
            orderService.fulfillOrder(TestUtils.getAllocatedOrder().getId(), response);
        } catch (ApiException e) {
            Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_ORDER_ID);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
}
