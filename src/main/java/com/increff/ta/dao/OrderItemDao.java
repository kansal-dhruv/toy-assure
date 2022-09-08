package com.increff.ta.dao;

import com.increff.ta.pojo.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {

    private final String find_by_orderId = "SELECT e from OrderItem e where e.orderId=:orderId";

    public OrderItem saveOrUpdate(OrderItem orderItem) {
        return em.merge(orderItem);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        TypedQuery<OrderItem> query = getQuery(find_by_orderId, OrderItem.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
