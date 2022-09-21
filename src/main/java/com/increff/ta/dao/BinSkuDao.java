package com.increff.ta.dao;

import com.increff.ta.pojo.BinSkuPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao {

  private final String select_by_globalSkuId = "SELECT e from BinSkuPojo e where e.globalSkuId=:globalSkuId ORDER BY e.quantity DESC";

  private final String select_by_binId_and_globalSkuId = "SELECT e from BinSkuPojo e where e.binId=:binId and e.globalSkuId=:globalSkuId";

  private final String select_total_count_by_globalSkuId = "SELECT SUM(e.quantity) from BinSkuPojo e where e.globalSkuId=:globalSkuId";

  public List<BinSkuPojo> findByglobalSkuId(Long globalSkuId) {
    TypedQuery<BinSkuPojo> query = getQuery(select_by_globalSkuId, BinSkuPojo.class);
    query.setParameter("globalSkuId", globalSkuId);
    return query.getResultList();
  }

  public BinSkuPojo findByBinIdAndGlobalSkuId(Long binId, Long globalSkuId) {
    TypedQuery<BinSkuPojo> query = getQuery(select_by_binId_and_globalSkuId, BinSkuPojo.class);
    query.setParameter("binId", binId);
    query.setParameter("globalSkuId", globalSkuId);
    return query.getResultList().stream().findFirst().orElse(null);
  }

  public Long findTotalCountByGlobalSkuId(Long globalSkuId) {
    TypedQuery<Long> query = getQuery(select_total_count_by_globalSkuId, Long.class);
    query.setParameter("globalSkuId", globalSkuId);
    return query.getResultList().stream().findFirst().orElse(null);

  }

  public BinSkuPojo insertOrUpdate(BinSkuPojo binSkuPojo) {
    return em.merge(binSkuPojo);
  }
}