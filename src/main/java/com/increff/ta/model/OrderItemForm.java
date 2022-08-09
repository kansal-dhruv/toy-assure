package com.increff.ta.model;

import java.io.Serializable;

public class OrderItemForm implements Serializable {
    private String channelSkuId;
    private Long quantity;

    public String getChannelSkuId() {
        return channelSkuId;
    }

    public void setChannelSkuId(String channelSkuId) {
        this.channelSkuId = channelSkuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}