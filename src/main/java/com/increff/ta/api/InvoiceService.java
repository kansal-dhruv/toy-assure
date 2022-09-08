package com.increff.ta.api;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.model.invoice.InvoiceData;
import com.increff.ta.model.invoice.InvoiceItemData;
import com.increff.ta.pojo.OrderItem;
import com.increff.ta.pojo.Orders;
import com.increff.ta.pojo.Product;
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

    @Autowired
    private ProductDao productDao;

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
        invoiceData.setInvoiceItemData(convertOrderItemToInvoiceOrderItem(orderItems));
        invoiceData.setInvoiceNumber(order.getId());
        invoiceData.setInvoiceDate(new Timestamp(System.currentTimeMillis()).toString());
        invoiceData.setClientId(order.getClientId().toString());
        invoiceData.setInvoiceTotal(orderItems.stream().mapToDouble(orderItem -> orderItem.getFullfilledQuanity() * orderItem.getSellingPricePerUnit()).sum());
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

    private List<InvoiceItemData> convertOrderItemToInvoiceOrderItem(List<OrderItem> orderItems) {
        List<InvoiceItemData> invoiceItemDataList = new ArrayList<InvoiceItemData>();
        for (OrderItem orderItem : orderItems) {

            InvoiceItemData invoiceItemData = new InvoiceItemData();
            Product product = productDao.findByGlobalSkuid(orderItem.getGlobalSkuId());
            invoiceItemData.setProductName(product.getName());
            invoiceItemData.setClientSkuid(product.getClientSkuId());
            invoiceItemData.setAmount(orderItem.getSellingPricePerUnit() * orderItem.getAllocatedQuanity());
            invoiceItemData.setQuantity(orderItem.getAllocatedQuanity());
            invoiceItemData.setSellingPricePerUnit(orderItem.getSellingPricePerUnit());
            invoiceItemDataList.add(invoiceItemData);
            orderItem.setFullfilledQuanity(orderItem.getAllocatedQuanity());
            orderItem.setAllocatedQuanity(0L);
            inventoryService.decrementAllocatedQuantity(product, orderItem.getOrderedQuantity());
            inventoryService.incrementFulFilledQuantity(product, orderItem.getOrderedQuantity());
        }
        return invoiceItemDataList;
    }

    private byte[] createInvoicePdf(String xml, File xslt) throws FOPException, TransformerException {
        try {
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));
            Source src = new StreamSource(new StringReader(xml));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
            return outputStream.toByteArray();
        } catch (Exception e){
            throw new ApiException(Constants.CODE_ISSUE_GENERATING_INVOICE, Constants.MSG_ISSUE_GENERATING_INVOICE);
        }
    }
}
