package com.increff.ta.api;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.ProductDao;
import com.increff.ta.dao.UserDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.pojo.Product;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductApi {

  @Autowired
  private ProductDao productDao;

  @Autowired
  private UserDao userDao;


  public void addProducts(List<Product> products) {
    for (Product product : products) {
      addProduct(product);
    }
  }

  public void addProduct(Product productToBeAdded) {
    User client = null;
    client = userDao.selectById(productToBeAdded.getClient());
    if (client == null || !client.getType().equals(UserType.CLIENT)) {
      throw new ApiException(Constants.CODE_INVALID_USER, Constants.MSG_INVALID_USER, "Input Client ID is " +//
          "invalid");
    }
    Product product = productDao//
        .findByClientSkuIdAndClientId(productToBeAdded.getClientSkuId(), productToBeAdded.getClient());
    if (product != null) {
      product.setName(productToBeAdded.getName());
      product.setMrp(productToBeAdded.getMrp());
      product.setBrandId(productToBeAdded.getBrandId());
      product.setDescription(productToBeAdded.getDescription());
      productDao.addProduct(product);
    } else {
      productDao.addProduct(productToBeAdded);
    }

  }


}