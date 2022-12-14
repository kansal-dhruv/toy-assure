package com.increff.ta.model;

import com.opencsv.bean.CsvBindByPosition;

public class BinClientSkuCSV {

    @CsvBindByPosition(position = 0, required = true)
    private Long binId;

    @CsvBindByPosition(position = 1, required = true)
    private String clientSkuId;

    @CsvBindByPosition(position = 2, required = true)
    private Long quantity;

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }

    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
