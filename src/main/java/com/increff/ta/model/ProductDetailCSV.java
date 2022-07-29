package com.increff.ta.model;

import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.UniqueConstraint;

@MappedSuperclass
public class ProductDetailCSV {

    @CsvBindByPosition(position = 0)
    @Column(unique = true)
    protected String clientSkuId;

    @CsvBindByPosition(position = 1)
    protected String name;

    @CsvBindByPosition(position = 2)
    protected String brandId;

    @CsvBindByPosition(position = 3)
    protected Double mrp;

    @CsvBindByPosition(position = 4)
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
