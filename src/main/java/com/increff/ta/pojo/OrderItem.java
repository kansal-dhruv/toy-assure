package com.increff.ta.pojo;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "assure_order_item")
public class OrderItem extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long globalSkuId;

    private Long orderedQuantity = 0L;

    @ColumnDefault(value = "0")
    private Long allocatedQuanity = 0L;

    @ColumnDefault(value = "0")
    private Long fullfilledQuanity = 0L;

    private Double sellingPricePerUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public Long getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public Long getAllocatedQuanity() {
        return allocatedQuanity;
    }

    public void setAllocatedQuanity(Long allocatedQuanity) {
        this.allocatedQuanity = allocatedQuanity;
    }

    public Long getFullfilledQuanity() {
        return fullfilledQuanity;
    }

    public void setFullfilledQuanity(Long fullfilledQuanity) {
        this.fullfilledQuanity = fullfilledQuanity;
    }

    public Double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }

    public void setSellingPricePerUnit(Double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }
}
