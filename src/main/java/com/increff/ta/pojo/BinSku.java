package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_bin_sku")
public class BinSku extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "binId")
    private Bin bin;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "globalSkuId")
    private Product product;

    private Long quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
