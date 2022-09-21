package com.increff.ta.dao;

import com.increff.ta.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InventoryDao extends AbstractDao {

  private final String select_by_globalSkuId = "SELECT e FROM InventoryPojo e where e.globalSkuId=:globalSkuId";
  private final String select_by_clientSkuId = "SELECT e FROM InventoryPojo e where e.clientSkuId=:clientSkuId";

  public InventoryPojo insertOrUpdate(InventoryPojo inv) {
    return em.merge(inv);
  }

  public InventoryPojo findByGlobalSkuid(Long globalSkuid) {
    TypedQuery<InventoryPojo> query = getQuery(select_by_globalSkuId, InventoryPojo.class);
    query.setParameter("globalSkuId", globalSkuid);

    return query.getResultList().stream().findFirst().orElse(null);
  }
}