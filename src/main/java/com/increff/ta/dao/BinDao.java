package com.increff.ta.dao;

import com.increff.ta.pojo.BinPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BinDao {

  @PersistenceContext
  private EntityManager em;

  public BinPojo create(BinPojo binPojo) {
    return em.merge(binPojo);
  }

  public BinPojo findById(Long binId) {
    return em.find(BinPojo.class, binId);
  }
}