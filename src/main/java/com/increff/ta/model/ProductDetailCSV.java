package com.increff.ta.model;

import com.increff.ta.utils.MustBePositiveNumber;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.validators.PreAssignmentValidator;

public class ProductDetailCSV {

    @CsvBindByPosition(position = 0, required = true)
    protected String clientSkuId;

    @CsvBindByPosition(position = 1, required = true)
    protected String name;

    @CsvBindByPosition(position = 2, required = true)
    protected String brandId;

    @PreAssignmentValidator(validator = MustBePositiveNumber.class)
    @CsvBindByPosition(position = 3, required = true)
    protected Double mrp;

    @CsvBindByPosition(position = 4, required = true)
    protected String description;

    public String getClientSkuId() {
        return clientSkuId;
    }

    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
