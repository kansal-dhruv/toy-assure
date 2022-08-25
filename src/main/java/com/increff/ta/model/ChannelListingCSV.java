package com.increff.ta.model;

import com.opencsv.bean.CsvBindByPosition;

public class ChannelListingCSV {

    @CsvBindByPosition(position = 0, required = true)
    private String channelSkuId;

    @CsvBindByPosition(position = 1, required = true)
    private String clientSkuId;

    public String getChannelSkuId() {
        return channelSkuId;
    }

    public void setChannelSkuId(String channelSkuId) {
        this.channelSkuId = channelSkuId;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }

    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }
}
