package com.increff.ta.dao;

import com.increff.ta.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class OrderDao extends AbstractDao {

  private final String find_by_channelOrderId = "SELECT e from OrderPojo e where e.channelOrderId=:channelOrderId";

  public OrderPojo saveOrUpdate(OrderPojo orderPojo) {
    return em.merge(orderPojo);
  }

  public OrderPojo findByChannelOrderId(String channelOrderId) {
    TypedQuery<OrderPojo> query = getQuery(find_by_channelOrderId, OrderPojo.class);
    query.setParameter("channelOrderId", channelOrderId);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  public OrderPojo findByOrderId(Long orderId) {
    return em.find(OrderPojo.class, orderId);
  }
}