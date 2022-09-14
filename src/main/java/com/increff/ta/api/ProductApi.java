package com.increff.ta.api;

import com.increff.ta.dao.ProductDao;
import com.increff.ta.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductApi {

  @Autowired
  private ProductDao productDao;

  public Product getProductByClientSkuID(String clientSkuId){
    return productDao.findByClientSkuId(clientSkuId);
  }

  public Product getProductByClientSkuIdAndClientId(String clientSkuId, Long clientId){
    return productDao.findByClientSkuIdAndClientId(clientSkuId, clientId);
  }

  public void addProduct(Product product) {
    productDao.addProduct(product);
  }


}