package com.increff.tests.ta.test;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.constants.Constants;
import com.increff.ta.controller.ChannelController;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.tests.ta.AbstractUnitTest;
import com.increff.tests.ta.utils.FileUtils;
import com.increff.tests.ta.utils.TestUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChannelControllerTest extends AbstractUnitTest {

  private final String basePathCSV = "csvs/channels/";

  @Autowired
  ChannelController channelController;

  @Autowired
  UserDao userDao;

  @Autowired
  ProductDao productDao;

  @Test
  public void Test1_createChannel(){
    channelController.addChannel(TestUtils.createChannelFormInternal());
  }

  @Test
  public void Test2_createChannelDuplicate(){
    channelController.addChannel(TestUtils.createChannelForm());
    try {
      channelController.addChannel(TestUtils.createChannelForm());
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INAVLID_CHANNEL);
    }
  }

  @Test
  public void Test3_createChannelListing(){
    String fileName = "addChannelListing-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    channelController.addChannelListing("FLIPKART", client.getName(), mpFile);
  }

  @Test
  public void Test4_createChannelListing(){
    String fileName = "addChannelListing-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      channelController.addChannelListing("FLIPKART", customer.getName(), mpFile);
    } catch (ApiException e){
      e.printStackTrace();
      Assert.assertEquals(e.getCode(), Constants.CODE_INVALID_USER);
    }
  }

  @Test
  public void Test5_createChannelListing() {
    String fileName = "addChannelListing-ok.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    }
    catch (IOException e) {
      Assert.fail();
    }
    try{
      channelController.addChannelListing("channelNotExisting", client.getName(), mpFile);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_INAVLID_CHANNEL);
    }
  }

  @Test
  public void Test6_createChannelListing(){
    String fileName = "addChannelListing-productNotExists.csv";
    MultipartFile mpFile = null;
    String filePath = basePathCSV + fileName;
    try {
      mpFile = FileUtils.loadCSV(filePath, fileName);
    } catch (IOException e){
      Assert.fail();
    }
    try {
      channelController.addChannelListing("FLIPKART", client.getName(), mpFile);
    } catch (ApiException e){
      e.printStackTrace();
      Assert.assertEquals(e.getCode(), Constants.CODE_PRODUCT_NOT_FOUND);
    }
  }


}