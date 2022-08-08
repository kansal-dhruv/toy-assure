package com.increff.ta.pojo;

import com.increff.ta.enums.ChannelInvoiceType;

import javax.persistence.*;

@Entity
@Table(name = "assure_channel")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ChannelInvoiceType invoiceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChannelInvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(ChannelInvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }
}
