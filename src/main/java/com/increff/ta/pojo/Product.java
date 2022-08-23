package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_product")
public class Product extends AbstractModel {

    @Column(unique = true)
    protected String clientSkuId;

    protected String name;

    protected String brandId;

    protected Double mrp;

    protected String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long globalSkuId;

    @OneToOne
    @JoinColumn(name = "client_id")
    private User client;

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

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
