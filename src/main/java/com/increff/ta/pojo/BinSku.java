package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_bin_sku")
public class BinSku extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long binId;

    private Long globalSkuId;

    private Long quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
