package com.increff.ta.api;

import com.increff.ta.dao.OrderDao;
import com.increff.ta.dao.OrderItemDao;
import com.increff.ta.pojo.OrderItem;
import com.increff.ta.pojo.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional
    public void createOrder(Orders orders, List<OrderItem> orderItemList) {
        orders = orderDao.saveOrUpdate(orders);
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orders.getId());
            orderItemDao.saveOrUpdate(orderItem);
        }
    }

    @Transactional
    public void fulfillOrder(Orders order) {
        orderDao.saveOrUpdate(order);

    }

    public Orders getOrderByChannelOrderId(String channelOrderId){
        return orderDao.findByChannelOrderId(channelOrderId);
    }

    public Orders getOrderByOrderId(Long orderId){
        return orderDao.findByOrderId(orderId);
    }

    public List<OrderItem> getItemListByOrderId(Long orderId){
        return orderItemDao.findByOrderId(orderId);
    }

    public Orders updateOrder(Orders order){
        return orderDao.saveOrUpdate(order);
    }

    public List<OrderItem> updateOrderItems(List<OrderItem> orderItems){
        List<OrderItem> updatedList = new ArrayList<>();
        for(OrderItem orderItem : orderItems){
            updatedList.add(updateOrderItem(orderItem));
        }
        return updatedList;
    }

    public OrderItem updateOrderItem(OrderItem orderItem){
        return orderItemDao.saveOrUpdate(orderItem);
    }


}