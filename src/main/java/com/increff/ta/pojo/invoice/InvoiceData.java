package com.increff.ta.pojo.invoice;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "invoice-record")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvoiceData {

    @XmlElement(name = "client-id")
    private String clientId;

    @XmlElement(name = "invoice-number")
    private Long invoiceNumber;

    @XmlElement(name = "invoice-date")
    private String invoiceDate;

    @XmlElement(name = "invoice-total")
    private Double invoiceTotal;

    @XmlElement(name = "line-item-record")
    @XmlElementWrapper(name = "line-item-records")
    private List<InvoiceItemData> invoiceItemData;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(Double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public List<InvoiceItemData> getInvoiceItemData() {
        return invoiceItemData;
    }

    public void setInvoiceItemData(List<InvoiceItemData> invoiceItemData) {
        this.invoiceItemData = invoiceItemData;
    }
}
