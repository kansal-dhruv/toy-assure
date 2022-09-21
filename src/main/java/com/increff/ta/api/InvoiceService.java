package com.increff.ta.api;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.invoice.InvoiceData;
import com.increff.ta.commons.model.invoice.InvoiceItemData;
import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.pojo.OrderItemPojo;
import com.increff.ta.pojo.OrderPojo;
import com.increff.ta.pojo.ProductPojo;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

  final private FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

  final private String xslTemplateName = "xslTemplate.xsl";

  private final InventoryApi inventoryApi;

  private final ProductDao productDao;

  @Autowired
  public InvoiceService(InventoryApi inventoryApi, ProductDao productDao) {
    this.inventoryApi = inventoryApi;
    this.productDao = productDao;
  }

  public byte[] generateInvoice(List<OrderItemPojo> orderItems, OrderPojo order) throws URISyntaxException {
    try {
      File xslFile = new File(Thread.currentThread().getContextClassLoader().getResource(xslTemplateName).toURI());
      String xmlInput = getXmlString(orderItems, order);
      return createInvoicePdf(xmlInput, xslFile);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ApiException(-14, "Issue while generating XML for orders");
    }

  }

  private String getXmlString(List<OrderItemPojo> orderItems, OrderPojo order) {
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
    }
    catch (Exception e) {
      throw new ApiException(-14, "Issue while generating XML for orders");
    }
    return stringWriter.toString();
  }

  private List<InvoiceItemData> convertOrderItemToInvoiceOrderItem(List<OrderItemPojo> orderItems) {
    List<InvoiceItemData> invoiceItemDataList = new ArrayList<InvoiceItemData>();
    for (OrderItemPojo orderItemPojo : orderItems) {

      InvoiceItemData invoiceItemData = new InvoiceItemData();
      ProductPojo productPojo = productDao.findByGlobalSkuid(orderItemPojo.getGlobalSkuId());
      invoiceItemData.setProductName(productPojo.getName());
      invoiceItemData.setClientSkuid(productPojo.getClientSkuId());
      invoiceItemData.setAmount(orderItemPojo.getSellingPricePerUnit() * orderItemPojo.getAllocatedQuanity());
      invoiceItemData.setQuantity(orderItemPojo.getAllocatedQuanity());
      invoiceItemData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
      invoiceItemDataList.add(invoiceItemData);
      orderItemPojo.setFullfilledQuanity(orderItemPojo.getAllocatedQuanity());
      orderItemPojo.setAllocatedQuanity(0L);
      inventoryApi.decrementAllocatedQuantity(productPojo, orderItemPojo.getOrderedQuantity());
      inventoryApi.incrementFulFilledQuantity(productPojo, orderItemPojo.getOrderedQuantity());
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
    }
    catch (Exception e) {
      throw new ApiException(Constants.CODE_ISSUE_GENERATING_INVOICE, Constants.MSG_ISSUE_GENERATING_INVOICE);
    }
  }
}