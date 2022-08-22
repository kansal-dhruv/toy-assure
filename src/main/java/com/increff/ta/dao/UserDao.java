package com.increff.ta.dao;

import com.increff.ta.pojo.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class UserDao extends AbstractDao{

    private final String select_by_name = "SELECT e from User e where e.name=:name";
    @Transactional
    public User insert(User user){return em.merge(user);}

    public User selectById(Long id){
        return em.find(User.class, id);
    }

    public User selectByName(String name){
        TypedQuery<User> query = getQuery(select_by_name, User.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
