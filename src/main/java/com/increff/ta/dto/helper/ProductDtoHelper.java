package com.increff.ta.dto.helper;

import com.increff.ta.commons.model.ProductDetailCSV;
import com.increff.ta.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {
  public static List<ProductPojo> convertCSVtoPojo(List<ProductDetailCSV> productDetailCSVList, Long clientId) {
    List<ProductPojo> productList = new ArrayList<>();
    for (ProductDetailCSV productDetailCSV : productDetailCSVList) {
      ProductPojo productPojo = new ProductPojo();
      productPojo.setClientSkuId(productDetailCSV.getClientSkuId());
      productPojo.setClientId(clientId);
      productPojo.setMrp(productDetailCSV.getMrp());
      productPojo.setDescription(productDetailCSV.getDescription());
      productPojo.setName(productDetailCSV.getName());
      productPojo.setBrandId(productDetailCSV.getBrandId());
      productList.add(productPojo);
    }
    return productList;
  }
}