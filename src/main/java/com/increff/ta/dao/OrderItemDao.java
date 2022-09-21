package com.increff.ta.dao;

import com.increff.ta.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {

  private final String find_by_orderId = "SELECT e from OrderItemPojo e where e.orderId=:orderId";

  public OrderItemPojo saveOrUpdate(OrderItemPojo orderItemPojo) {
    return em.merge(orderItemPojo);
  }

  public List<OrderItemPojo> findByOrderId(Long orderId) {
    TypedQuery<OrderItemPojo> query = getQuery(find_by_orderId, OrderItemPojo.class);
    query.setParameter("orderId", orderId);
    return query.getResultList();
  }
}