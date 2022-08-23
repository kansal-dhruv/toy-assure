package com.increff.ta.service;

import com.increff.ta.pojo.OrderItem;
import com.increff.ta.pojo.Orders;
import com.increff.ta.pojo.invoice.InvoiceData;
import com.increff.ta.pojo.invoice.InvoiceItemData;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

    final private FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
    final private String xslTemplateName = "xslTemplate.xsl";
    @Autowired
    private InventoryService inventoryService;

    public byte[] generateInvoice(List<OrderItem> orderItems, Orders order) throws URISyntaxException {
        File xslFile = new File(Thread.currentThread().getContextClassLoader().getResource(xslTemplateName).toURI());
        String xmlInput = getXmlString(orderItems, order);
        try {
            return createInvoicePdf(xmlInput, xslFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(-14, "Issue while generating XML for orders");
        }

    }

    private String getXmlString(List<OrderItem> orderItems, Orders order) {
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceItemData(convertOrderitemToInvoiceOrderItem(orderItems));
        invoiceData.setInvoiceNumber(order.getId());
        invoiceData.setInvoiceDate(new Timestamp(System.currentTimeMillis()).toString());
        invoiceData.setClientId(order.getClient().getName());
        invoiceData.setInvoiceTotal(orderItems.stream().mapToDouble(orderItem -> orderItem.getAllocatedQuanity() * orderItem.getSellingPricePerUnit()).sum());
        StringWriter stringWriter = new StringWriter();
        //Convert to XML String

        try {
            JAXBContext context = JAXBContext.newInstance(InvoiceData.class);
            Marshaller marshallerObj = context.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.marshal(invoiceData, stringWriter);
        } catch (Exception e) {
            throw new ApiException(-14, "Issue while generating XML for orders");
        }
        return stringWriter.toString();
    }

    private List<InvoiceItemData> convertOrderitemToInvoiceOrderItem(List<OrderItem> orderItems) {
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<InvoiceItemData>();
        for (OrderItem orderItem : orderItems) {
            InvoiceItemData invoiceItemData = new InvoiceItemData();
            invoiceItemData.setProductName(orderItem.getProduct().getName());
            invoiceItemData.setClientSkuid(orderItem.getProduct().getClientSkuId());
            invoiceItemData.setAmount(orderItem.getSellingPricePerUnit() * orderItem.getAllocatedQuanity());
            invoiceItemData.setQuantity(orderItem.getAllocatedQuanity());
            invoiceItemData.setSellingPricePerUnit(orderItem.getSellingPricePerUnit());
            invoiceItemDataList.add(invoiceItemData);
            orderItem.setFullfilledQuanity(orderItem.getAllocatedQuanity());
            orderItem.setAllocatedQuanity(0L);
            inventoryService.decrementAllocatedQuantity(orderItem.getProduct(), orderItem.getOrderedQuantity());
            inventoryService.incrementFulFilledQuantity(orderItem.getProduct(), orderItem.getOrderedQuantity());
        }
        return invoiceItemDataList;
    }

    private byte[] createInvoicePdf(String xml, File xslt) throws IOException, FOPException, TransformerException {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xslt));
        Source src = new StreamSource(new StringReader(xml));
        Result res = new SAXResult(fop.getDefaultHandler());
        transformer.transform(src, res);
        return outputStream.toByteArray();
    }
}
