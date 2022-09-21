package com.increff.ta.api;

import com.increff.ta.dao.OrderDao;
import com.increff.ta.dao.OrderItemDao;
import com.increff.ta.pojo.OrderItemPojo;
import com.increff.ta.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderApi {

  private final OrderDao orderDao;

  private final OrderItemDao orderItemDao;

  @Autowired
  public OrderApi(OrderDao orderDao, OrderItemDao orderItemDao) {
    this.orderDao = orderDao;
    this.orderItemDao = orderItemDao;
  }

  public void createOrder(OrderPojo orderPojo, List<OrderItemPojo> orderItemList) {
    orderPojo = orderDao.saveOrUpdate(orderPojo);
    for (OrderItemPojo orderItemPojo : orderItemList) {
      orderItemPojo.setOrderId(orderPojo.getId());
      orderItemDao.saveOrUpdate(orderItemPojo);
    }
  }

  public void fulfillOrder(OrderPojo order) {
    orderDao.saveOrUpdate(order);

  }

  public OrderPojo getOrderByChannelOrderId(String channelOrderId) {
    return orderDao.findByChannelOrderId(channelOrderId);
  }

  public OrderPojo getOrderByOrderId(Long orderId) {
    return orderDao.findByOrderId(orderId);
  }

  public List<OrderItemPojo> getItemListByOrderId(Long orderId) {
    return orderItemDao.findByOrderId(orderId);
  }
}