package com.increff.tests.ta.test;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.constants.Constants;
import com.increff.ta.controller.OrderController;
import com.increff.tests.ta.AbstractUnitTest;
import com.increff.tests.ta.utils.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderControllerCSVTest extends AbstractUnitTest {

  private final String basePathCSV = "csvs/orders/";

  @Autowired
  OrderController orderController;

  @Test
  public void Test1_createOrderCSV(){
    String fileName = "createOrderInternal-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    orderController.orderUpload(client.getId(), "order-i-1", customer.getId(), mpFile);
  }

  @Test
  public void Test2_createOrderCSVInvalidUser(){
    String fileName = "createOrderInternal-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      orderController.orderUpload(customer.getId(), "order-i-1", client.getId(), mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
    }
  }

  @Test
  public void Test3_createOrderCSVInvalidCSV(){
    String fileName = "createOrderInternal-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCsvWithoutExtension(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      orderController.orderUpload(client.getId(), "order-i-1", customer.getId(), mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
    }
  }

  @Test
  public void Test4_createOrderCSVInvalidChannelOrderId(){
    String fileName = "createOrderInternal-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      orderController.orderUpload(client.getId(), "order-i-1", customer.getId(), mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CHANNEL_ORDER_ID);
    }
  }

  @Test
  public void Test5_allocateOrder(){
    orderController.allocateOrder(1L);
  }

  @Test
  public void Test5_allocateOrderInvalidOrderId(){
    try {
      orderController.allocateOrder(2L);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_ORDER_ID);
    }
  }

  @Test
  public void Test6_fulfillOrder(){
    MockHttpServletResponse response = new MockHttpServletResponse();
    orderController.fulfillOrder(1L, response);
  }

  @Test
  public void Test6_fulfillOrderInvalidOrderId(){
    MockHttpServletResponse response = new MockHttpServletResponse();
    try {
      orderController.fulfillOrder(2L, response);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_ORDER_ID);
    }
  }

  @Test
  public void Test7_createOrderCSV(){
    String fileName = "createOrderInternal-error-productNotExists.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      orderController.orderUpload(client.getId(), "order-i-2", customer.getId(), mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
    }
  }

}