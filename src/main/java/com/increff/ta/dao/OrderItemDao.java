package com.increff.ta.dao;

import com.increff.ta.pojo.OrderItem;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao{

    private final String find_by_orderId = "SELECT e from OrderItem e where e.orders.id=:orderId";
    public OrderItem saveOrUpdate(OrderItem orderItem){
        return em.merge(orderItem);
    }

    public List<OrderItem> findByOrderId(Long orderId){
        TypedQuery<OrderItem> query = getQuery(find_by_orderId, OrderItem.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
