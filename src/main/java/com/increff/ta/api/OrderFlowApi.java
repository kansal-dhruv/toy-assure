package com.increff.ta.api;

import com.increff.ta.pojo.Inventory;
import com.increff.ta.pojo.OrderItem;
import com.increff.ta.pojo.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderFlowApi {

  @Autowired
  private InventoryService inventoryService;

  @Autowired
  private OrderApi orderApi;

  @Transactional
  public void allocateOrderFlow(Inventory inventory, Orders order, List<OrderItem> orderitemList){
    inventoryService.updateInventory(inventory);
    orderApi.updateOrder(order);
    orderApi.updateOrderItems(orderitemList);

  }

}