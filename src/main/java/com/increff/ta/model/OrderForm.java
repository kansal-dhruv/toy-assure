package com.increff.ta.model;

import java.io.Serializable;
import java.util.List;

public class OrderForm implements Serializable {

    private Long clientId;
    private Long customerId;
    private String channelName;
    private String channelOrderId;
    private List<OrderItemForm> orderItemList;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public List<OrderItemForm> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemForm> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
