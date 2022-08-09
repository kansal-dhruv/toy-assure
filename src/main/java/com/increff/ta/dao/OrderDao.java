package com.increff.ta.dao;

import com.increff.ta.pojo.Orders;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class OrderDao extends AbstractDao {

    private final String find_by_channelOrderId = "SELECT e from Orders e where e.channelOrderId=:channelOrderId";

    public Orders saveOrUpdate(Orders orders) {
        return em.merge(orders);
    }

    public Orders findByChannelOrderId(String channelOrderId){
        TypedQuery<Orders> query = getQuery(find_by_channelOrderId, Orders.class);
        query.setParameter("channelOrderId", channelOrderId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public Orders findByOrderId(Long orderId){
        return em.find(Orders.class, orderId);
    }
}
