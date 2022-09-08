package com.increff.ta.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class OrderItemForm implements Serializable {

    @NotBlank
    private String channelSkuId;

    @Min(0)
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