package com.increff.ta.dao;

import com.increff.ta.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

  private final String select_by_clientSkuId = "SELECT e from ProductPojo e where e.clientSkuId=:clientSkuId";

  private final String select_by_clientSkuId_and_ClientId = "SELECT e from ProductPojo e where e" +
      ".clientSkuId=:clientSkuId" +
      " and e.clientId=:clientId";

  private final String select_by_globalSkuId = "SELECT e FROM ProductPojo e where e.globalSkuId=:globalSkuId";

  private final String select_by_clientId = "SELECT e FROM ProductPojo e where e.clientId=:clientId";
  public ProductPojo addProduct(ProductPojo productPojo) {
    return em.merge(productPojo);
  }

  public ProductPojo findByClientSkuId(String clientSkuId) {
    TypedQuery<ProductPojo> query = getQuery(select_by_clientSkuId, ProductPojo.class);
    query.setParameter("clientSkuId", clientSkuId);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  public ProductPojo findByClientSkuIdAndClientId(String clientSkuId, Long clientId) {
    TypedQuery<ProductPojo> query = getQuery(select_by_clientSkuId_and_ClientId, ProductPojo.class);
    query.setParameter("clientSkuId", clientSkuId);
    query.setParameter("clientId", clientId);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  public ProductPojo findByGlobalSkuid(Long globalSkuid) {
    TypedQuery<ProductPojo> query = getQuery(select_by_globalSkuId, ProductPojo.class);
    query.setParameter("globalSkuId", globalSkuid);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  public List<ProductPojo> findByClientId(Long clientId){
    TypedQuery<ProductPojo> query = getQuery(select_by_clientId, ProductPojo.class);
    query.setParameter("clientId", clientId);
    return query.getResultList();
  }
}