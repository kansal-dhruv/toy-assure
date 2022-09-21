package com.increff.ta.api;

import com.increff.ta.dao.ProductDao;
import com.increff.ta.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductApi {

  private final ProductDao productDao;

  @Autowired
  public ProductApi(ProductDao productDao) {
    this.productDao = productDao;
  }

  public ProductPojo getProductByClientSkuID(String clientSkuId) {
    return productDao.findByClientSkuId(clientSkuId);
  }

  public ProductPojo getProductByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
    return productDao.findByClientSkuIdAndClientId(clientSkuId, clientId);
  }

  public void addProduct(ProductPojo productPojo) {
    productDao.addProduct(productPojo);
  }


}