package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_inventory")
public class Inventory extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long globalSkuId;

    private Long availableQuantity = 0L;

    private Long allocatedQuantity = 0L;

    private Long fulfilledQuantity = 0L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public void setAllocatedQuantity(Long allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    public Long getFulfilledQuantity() {
        return fulfilledQuantity;
    }

    public void setFulfilledQuantity(Long fulfilledQuantity) {
        this.fulfilledQuantity = fulfilledQuantity;
    }
}
