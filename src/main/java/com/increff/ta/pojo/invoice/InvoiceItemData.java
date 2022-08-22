package com.increff.ta.pojo.invoice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;



@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceItemData {

    @XmlElement(name = "client-sku-id")
    private String clientSkuid;

    @XmlElement(name = "product-name")
    private String productName;

    @XmlElement(name = "quantity")
    private Long quantity;

    @XmlElement(name = "selling-price-per-unit")
    private Double sellingPricePerUnit;

    @XmlElement(name = "amount")
    private Double amount;

    public String getClientSkuid() {
        return clientSkuid;
    }

    public void setClientSkuid(String clientSkuid) {
        this.clientSkuid = clientSkuid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }

    public void setSellingPricePerUnit(Double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
