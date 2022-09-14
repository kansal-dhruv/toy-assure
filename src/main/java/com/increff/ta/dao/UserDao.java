package com.increff.ta.dao;

import com.increff.ta.enums.UserType;
import com.increff.ta.pojo.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class UserDao extends AbstractDao {

    private final String select_by_name_and_type = "SELECT e from User e where e.name=:name and e.type=:type";

    public User insert(User user) {
        return em.merge(user);
    }

    public User selectById(Long id) {
        return em.find(User.class, id);
    }

    public User selectByNameAndType(String name, UserType userType){
        TypedQuery<User> query = getQuery(select_by_name_and_type, User.class);
        query.setParameter("name", name);
        query.setParameter("type", userType);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}