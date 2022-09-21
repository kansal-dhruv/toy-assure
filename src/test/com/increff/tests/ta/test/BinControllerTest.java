package com.increff.tests.ta.test;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.constants.Constants;
import com.increff.ta.controller.BinController;
import com.increff.ta.dao.InventoryDao;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.tests.ta.AbstractUnitTest;
import com.increff.tests.ta.utils.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BinControllerTest extends AbstractUnitTest {

  private final String basePathCSV = "csvs/bins/";

  @Autowired
  BinController binController;

  @Autowired
  UserDao userDao;

  @Autowired
  ProductDao productDao;

  @Autowired
  InventoryDao inventoryDao;

  @Test
  public void Test1_createBins(){
    binController.createUser(5);
  }

  @Test
  public void createZeroBins(){
    try {
      binController.createUser(0);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_BIN_COUNT);
    }
  }

  @Test
  public void createNegativeBins(){
    try {
      binController.createUser(-1);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_BIN_COUNT);
    }
  }

  @Test
  public void Test2_putProductToBin(){
    String fileName = "AddProductsToBin-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    binController.putProducts(mpFile);
    Assert.assertEquals(Optional.of(25L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new1").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(5L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new2").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(10L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new3").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(12L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new4").getGlobalSkuId()).getAvailableQuantity());
  }

  @Test
  public void Test3_putProductToBin(){
    String fileName = "AddProductsToBIn-error-BinNotExists.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      binController.putProducts(mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_BIN_NOT_FOUND);
    }
  }

  @Test
  public void Test4_putProductToBin(){
    String fileName = "AddProductsToBIn-error-productNotExists.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      binController.putProducts(mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
    }
  }

  @Test
  public void Test5_putProductToBin(){
    String fileName = "AddProductsToBin-ok-2.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    binController.putProducts(mpFile);
    Assert.assertEquals(Optional.of(30L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new1").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(5L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new2").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(10L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new3").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(12L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new4").getGlobalSkuId()).getAvailableQuantity());
    Assert.assertEquals(Optional.of(20L).get(),
        inventoryDao.findByGlobalSkuid(productDao.findByClientSkuId("new5").getGlobalSkuId()).getAvailableQuantity());
  }

  @Test
  public void putProductToBinInvalidCsvExtension(){
    String fileName = "AddProductsToBin-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCsvWithoutExtension(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      binController.putProducts(mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
    }
  }

  @Test
  public void putProductToBinWithValidationErrors(){
    String fileName = "AddProductsToBin-error.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      binController.putProducts(mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_ERROR_PARSING_CSV_FILE);
    }
  }

  @Test
  public void putProductToBinWithDuplicateEntry(){
    String fileName = "AddProductsToBin-error-duplicate.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      binController.putProducts(mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_BINID_CLIENTSKUID_SHOULD_BE_UNIQUE);
    }
  }
}