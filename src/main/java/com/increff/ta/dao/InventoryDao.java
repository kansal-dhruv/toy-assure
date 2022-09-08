package com.increff.ta.dao;

import com.increff.ta.pojo.Inventory;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class InventoryDao extends AbstractDao {

    private final String select_by_globalSkuId = "SELECT e FROM Inventory e where e.globalSkuId=:globalSkuId";
    private final String select_by_clientSkuId = "SELECT e FROM Inventory e where e.clientSkuId=:clientSkuId";

    public Inventory insertOrUpdate(Inventory inv) {
        return em.merge(inv);
    }

    public Inventory findByGlobalSkuid(Long globalSkuid) {
        TypedQuery<Inventory> query = getQuery(select_by_globalSkuId, Inventory.class);
        query.setParameter("globalSkuId", globalSkuid);
        return query.getResultList().stream().findFirst().orElse(null);
    }


    public Inventory findByClientSkuid(String clientSkuId) {
        TypedQuery<Inventory> query = getQuery(select_by_clientSkuId, Inventory.class);
        query.setParameter("clientSkuId", clientSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
