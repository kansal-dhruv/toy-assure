package com.increff.ta.pojo;

import com.increff.ta.model.ProductDetailCSV;

import javax.persistence.*;

@Entity
@Table(name="assure_product")
public class Product extends ProductDetailCSV {

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
}
