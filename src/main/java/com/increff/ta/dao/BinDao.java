package com.increff.ta.dao;

import com.increff.ta.pojo.Bin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class BinDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Bin create(Bin bin) {
        return em.merge(bin);
    }

    public Bin findById(Long binId) {
        return em.find(Bin.class, binId);
    }
}
