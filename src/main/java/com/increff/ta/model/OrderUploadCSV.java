package com.increff.ta.model;

import com.increff.ta.utils.MustBePositiveNumber;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.validators.PreAssignmentValidator;

public class OrderUploadCSV {

    @CsvBindByPosition(position = 0, required = true)
    private String clientSkuId;

    @PreAssignmentValidator(validator = MustBePositiveNumber.class)
    @CsvBindByPosition(position = 1, required = true)
    private Long orderedQuantity;

    @PreAssignmentValidator(validator = MustBePositiveNumber.class)
    @CsvBindByPosition(position = 2, required = true)
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
