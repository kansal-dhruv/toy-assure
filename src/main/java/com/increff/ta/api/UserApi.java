package com.increff.ta.api;

import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.dao.UserDao;
import com.increff.ta.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserApi {

  private final UserDao userDao;

  @Autowired
  public UserApi(UserDao userDao) {
    this.userDao = userDao;
  }

  public UserPojo createUser(UserPojo userPojo) {
    return userDao.insert(userPojo);
  }

  public UserPojo getUserById(Long userId) {
    return userDao.selectById(userId);
  }

  public UserPojo getUserByNameAndType(String userName, UserType type) {
    return userDao.selectByNameAndType(userName, type);
  }

}