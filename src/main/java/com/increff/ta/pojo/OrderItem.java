package com.increff.ta.pojo;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "assure_order_item")
public class OrderItem extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "globalSkuId")
    private Product product;

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

    public Orders getOrder() {
        return orders;
    }

    public void setOrder(Orders orders) {
        this.orders = orders;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
