package com.increff.ta.dao;

import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class UserDao extends AbstractDao {

  private final String select_by_name_and_type = "SELECT e from UserPojo e where e.name=:name and e.type=:type";

  public UserPojo insert(UserPojo userPojo) {
    return em.merge(userPojo);
  }

  public UserPojo selectById(Long id) {
    return em.find(UserPojo.class, id);
  }

  public UserPojo selectByNameAndType(String name, UserType userType) {
    TypedQuery<UserPojo> query = getQuery(select_by_name_and_type, UserPojo.class);
    query.setParameter("name", name);
    query.setParameter("type", userType);
    return query.getResultList().stream().findFirst().orElse(null);
  }
}