package com.increff.ta.model;

import com.increff.ta.enums.ChannelInvoiceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ChannelForm implements Serializable {

    @NotBlank
    private String name;

    @NotNull(message = " can be SELF/CHANNEL")
    private ChannelInvoiceType invoiceType;

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