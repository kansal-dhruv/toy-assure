package com.increff.ta.service;

import com.increff.ta.dao.InventoryDao;
import com.increff.ta.pojo.Inventory;
import com.increff.ta.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    public void updateAvailableQuantity(Product product, Long totalQuantity){
        Inventory inv = inventoryDao.findByGlobalSkuid(product.getGlobalSkuId());
        if(inv == null){
            inv = new Inventory();
            inv.setAvailableQuantity(totalQuantity);
            inv.setProduct(product);
            inventoryDao.insertOrUpdate(inv);
        } else {
            inv.setAvailableQuantity(totalQuantity);
        }
    }

    public void incrementAllocatedQuantity(Product product, Long quantity){
        Inventory inv = inventoryDao.findByGlobalSkuid(product.getGlobalSkuId());
        inv.setAllocatedQuantity(inv.getAllocatedQuantity() + quantity);
    }

    public void incrementFulFilledQuantity(Product product, Long quantity){
        Inventory inv = inventoryDao.findByGlobalSkuid(product.getGlobalSkuId());
        inv.setFulfilledQuantity(inv.getFulfilledQuantity() + quantity);
    }

    public Inventory getInventoryByClientSkuId(String clientSkuId){
        return inventoryDao.findByClientSkuid(clientSkuId);
    }
}
