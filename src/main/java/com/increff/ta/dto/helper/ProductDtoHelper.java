package com.increff.ta.dto.helper;

import com.increff.ta.model.ProductDetailCSV;
import com.increff.ta.pojo.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoHelper {
  public static List<Product> convertCSVtoPojo(List<ProductDetailCSV> productDetailCSVList, Long clientId) {
    List<Product> productList = new ArrayList<>();
    for(ProductDetailCSV productDetailCSV : productDetailCSVList){
      Product product = new Product();
      product.setClientSkuId(productDetailCSV.getClientSkuId());
      product.setClientId(clientId);
      product.setMrp(productDetailCSV.getMrp());
      product.setDescription(productDetailCSV.getDescription());
      product.setName(productDetailCSV.getName());
      product.setBrandId(productDetailCSV.getBrandId());
      productList.add(product);
    }
    return productList;
  }
}