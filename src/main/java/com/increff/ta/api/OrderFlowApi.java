package com.increff.ta.api;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.enums.OrderStatus;
import com.increff.ta.constants.Constants;
import com.increff.ta.pojo.BinSkuPojo;
import com.increff.ta.pojo.InventoryPojo;
import com.increff.ta.pojo.OrderItemPojo;
import com.increff.ta.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class OrderFlowApi {

  private final InventoryApi inventoryApi;

  private final OrderApi orderApi;

  private final BinApi binApi;

  @Autowired
  public OrderFlowApi(InventoryApi inventoryApi, OrderApi orderApi, BinApi binApi) {
    this.inventoryApi = inventoryApi;
    this.orderApi = orderApi;
    this.binApi = binApi;
  }

  @Transactional
  public void allocateOrderFlow(Long orderId) {
    OrderPojo order = orderApi.getOrderByOrderId(orderId);
    boolean areAllOrderItemsAllocated = true;
    if (Objects.nonNull(order) && order.getStatus().equals(OrderStatus.CREATED)) {
      List<OrderItemPojo> orderItemList = orderApi.getItemListByOrderId(orderId);
      for (OrderItemPojo orderItemPojo : orderItemList) {
        InventoryPojo inv = inventoryApi.getInventoryByGlobalSkuId(orderItemPojo.getGlobalSkuId());
        if (inv == null) {
          throw new ApiException(Constants.CODE_ITEM_NOT_IN_INVENTORY, Constants.MSG_ITEM_NOT_IN_INVENTORY,
              "ProductPojo with globalSkuId: " + orderItemPojo.getGlobalSkuId() + " id not present in inventory");
        }
        areAllOrderItemsAllocated = updateInventoryAndBins(areAllOrderItemsAllocated, orderItemPojo, inv);
      }
      if (areAllOrderItemsAllocated) {
        order.setStatus(OrderStatus.ALLOCATED);
      }
    } else {
      if (Objects.isNull(order))
        throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID,
            "Order Id is not valid");
      else
        throw new ApiException(Constants.CODE_INVALID_ORDER_ID, Constants.MSG_INVALID_ORDER_ID,
            "Order status is not created");
    }
  }

  private boolean updateInventoryAndBins(boolean areAllOrderItemsAllocated, OrderItemPojo orderItemPojo, InventoryPojo inv) {
    Long allocationAmount = Math.min(inv.getAvailableQuantity(), orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuanity());
    inv.setAvailableQuantity(inv.getAvailableQuantity() - allocationAmount);
    inv.setAllocatedQuantity(inv.getAllocatedQuantity() + allocationAmount);
    orderItemPojo.setAllocatedQuanity(orderItemPojo.getAllocatedQuanity() + allocationAmount);
    if (!Objects.equals(orderItemPojo.getAllocatedQuanity(), orderItemPojo.getOrderedQuantity())) {
      areAllOrderItemsAllocated = false;
    }
    List<BinSkuPojo> binSkuList = binApi.getBinsWithProduct(orderItemPojo.getGlobalSkuId());
    int binCount = 0;
    while (allocationAmount != 0) {
      BinSkuPojo selectedBin = binSkuList.get(binCount);
      Long availAmount = Math.min(allocationAmount, selectedBin.getQuantity());
      allocationAmount = allocationAmount - availAmount;
      selectedBin.setQuantity(selectedBin.getQuantity() - availAmount);
      binCount++;
    }
    return areAllOrderItemsAllocated;
  }

}