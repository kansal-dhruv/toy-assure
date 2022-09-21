package com.increff.tests.ta.utils;

import com.increff.ta.commons.model.ChannelForm;
import com.increff.ta.commons.model.enums.ChannelInvoiceType;
import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.pojo.ProductPojo;
import com.increff.ta.pojo.UserPojo;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
  public static UserPojo createClientUser(){
    UserPojo userPojo = new UserPojo();
    userPojo.setName("DhruvClient");
    userPojo.setType(UserType.CLIENT);
    return userPojo;
  }

  public static UserPojo createCustomerUser(){
    UserPojo userPojo = new UserPojo();
    userPojo.setName("DhruvCustomer");
    userPojo.setType(UserType.CUSTOMER);
    return userPojo;
  }

  public static List<ProductPojo> getProductList(){
    List<ProductPojo> productPojoList = new ArrayList<>();
    productPojoList.add(createProduct("new1", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new2", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new3", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new4", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new5", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new6", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new7", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    productPojoList.add(createProduct("new8", "jeans1", "brnd1", 2000D, "Blue Jeans", createClientUser().getId()));
    return productPojoList;
  }

  public static ProductPojo createProduct(String clientSkuId, String name, String brandId,
                                          Double mrp, String desc, Long clientId){
    ProductPojo productPojo = new ProductPojo();
    productPojo.setClientSkuId(clientSkuId);
    productPojo.setName(name);
    productPojo.setBrandId(brandId);
    productPojo.setMrp(mrp);
    productPojo.setDescription(desc);
    productPojo.setClientId(clientId);
    return productPojo;
  }

  public static ChannelForm createChannelFormInternal(){
    ChannelForm channelForm = new ChannelForm();
    channelForm.setName("INTERNAL");
    channelForm.setInvoiceType(ChannelInvoiceType.SELF);
    return channelForm;
  }

  public static ChannelForm createChannelForm(){
    ChannelForm channelForm = new ChannelForm();
    channelForm.setName("FLIPKART");
    channelForm.setInvoiceType(ChannelInvoiceType.CHANNEL);
    return channelForm;
  }

}