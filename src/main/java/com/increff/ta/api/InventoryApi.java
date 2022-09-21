package com.increff.ta.api;

import com.increff.ta.dao.InventoryDao;
import com.increff.ta.pojo.InventoryPojo;
import com.increff.ta.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
public class InventoryApi {

  private final InventoryDao inventoryDao;

  @Autowired
  public InventoryApi(InventoryDao inventoryDao) {
    this.inventoryDao = inventoryDao;
  }

  public void updateAvailableQuantity(ProductPojo productPojo, Long totalQuantity) {
    InventoryPojo inv = inventoryDao.findByGlobalSkuid(productPojo.getGlobalSkuId());
    if (Objects.isNull(inv)) {
      inv = new InventoryPojo();
      inv.setAvailableQuantity(totalQuantity);
      inv.setGlobalSkuId(productPojo.getGlobalSkuId());
      inventoryDao.insertOrUpdate(inv);
    } else {
      inv.setAvailableQuantity(totalQuantity);
    }
  }

  public void incrementFulFilledQuantity(ProductPojo productPojo, Long quantity) {
    InventoryPojo inv = inventoryDao.findByGlobalSkuid(productPojo.getGlobalSkuId());
    inv.setFulfilledQuantity(inv.getFulfilledQuantity() + quantity);
  }

  public void decrementAllocatedQuantity(ProductPojo productPojo, Long quantity) {
    InventoryPojo inv = inventoryDao.findByGlobalSkuid(productPojo.getGlobalSkuId());
    inv.setAllocatedQuantity(inv.getAllocatedQuantity() - quantity);
  }

  public InventoryPojo getInventoryByGlobalSkuId(Long globalSkuId) {
    return inventoryDao.findByGlobalSkuid(globalSkuId);
  }
}