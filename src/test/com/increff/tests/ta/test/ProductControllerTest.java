package com.increff.tests.ta.test;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.constants.Constants;
import com.increff.ta.controller.ProductController;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.pojo.ProductPojo;
import com.increff.tests.ta.AbstractUnitTest;
import com.increff.tests.ta.utils.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FixMethodOrder
public class ProductControllerTest extends AbstractUnitTest {

  private final String basePathCSV = "csvs/products/";

  @Autowired
  ProductController productController;

  @Autowired
  UserDao userDao;

  @Autowired
  ProductDao productDao;

  @Test
  public void addProductsAsClient(){
    String fileName = "addProducts-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    productController.addProducts(mpFile, client.getId());
    List<ProductPojo> products = productDao.findByClientId(client.getId());
    Assert.assertEquals(products.size(), 9);
  }

  @Test
  public void addProductsAsCustomer(){
    String fileName = "addProducts-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      productController.addProducts(mpFile, customer.getId());
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
    }
  }

  @Test
  public void updateProducts(){
    String fileName = "addProducts-updateexisting-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    productController.addProducts(mpFile, client.getId());
    System.out.print(productDao.findByClientId(client.getId()).size());
    ProductPojo product = productDao.findByClientSkuId("new1");
    Assert.assertEquals(product.getDescription(), "Blue Jeans edited");
  }

  @Test
  public void addProductsWithValidationErrors(){
    String fileName = "addProducts-error-validations.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      productController.addProducts(mpFile, client.getId());
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
    }
  }

  @Test
  public void addProductsWithRepeatedProduct(){
    String fileName = "addProducts-error-repeatedSKUID.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      productController.addProducts(mpFile, client.getId());
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_DUPLICATE_CLIENT_SKU_ID);
    }
  }
  @Test
  public void addProductsAsClientWithInvalidCSV(){
    String fileName = "addProducts-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCsvWithoutExtension(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      productController.addProducts(mpFile, client.getId());
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
    }
  }




}