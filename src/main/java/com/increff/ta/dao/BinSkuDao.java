package com.increff.ta.dao;

import com.increff.ta.pojo.BinSku;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class BinSkuDao extends AbstractDao {

    private final String select_by_globalSkuId = "SELECT e from BinSku e where e.globalSkuId=:globalSkuId";

    private final String select_by_binId_and_globalSkuId = "SELECT e from BinSku e where e.bin.binId=:binId and e.product.globalSkuId=:globalSkuId";

    private final String select_total_count_by_globalSkuId = "SELECT SUM(e.quantity) from BinSku e where e.product.globalSkuId=:globalSkuId";
    @Transactional
    public void insert(BinSku binSku) {
        em.persist(binSku);
    }

    public BinSku findByglobalSkuId(Long globalSkuId) {
        TypedQuery<BinSku> query = getQuery(select_by_globalSkuId, BinSku.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public BinSku findByBinIdAndGlobalSkuId(Long binId,Long globalSkuId) {
        TypedQuery<BinSku> query = getQuery(select_by_binId_and_globalSkuId, BinSku.class);
        query.setParameter("binId", binId);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public Long findTotalCountByGlobalSkuId(Long globalSkuId){
        TypedQuery<Long> query = getQuery(select_total_count_by_globalSkuId, Long.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);

    }

    @Transactional
    public void insertOrUpdate(BinSku binSku) {
        em.merge(binSku);
    }
}
