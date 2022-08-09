package com.increff.ta.model;

import com.opencsv.bean.CsvBindByPosition;

public class OrderUploadCSV {

    @CsvBindByPosition(position = 0)
    private String clientSkuId;

    @CsvBindByPosition(position = 1)
    private Long orderedQuantity;

    @CsvBindByPosition(position = 2)
    private Double sellingPricePerUnit;

    public String getClientSkuId() {
        return clientSkuId;
    }

    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public Long getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public Double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }

    public void setSellingPricePerUnit(Double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }
}
