package com.increff.ta.dao;

import com.increff.ta.pojo.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(User user){em.persist(user);}

    public User selectById(Long id){
        return em.find(User.class, id);
    }
}
