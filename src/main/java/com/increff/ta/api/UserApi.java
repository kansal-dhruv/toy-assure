package com.increff.ta.api;

import com.increff.ta.dao.UserDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApi {

    @Autowired
    private UserDao userDao;

    public void createUser(User user) {
        userDao.insert(user);
    }

    public User getUserById(Long userId){
        return userDao.selectById(userId);
    }

    public User getUserByNameAndType(String userName, UserType type){
        return userDao.selectByNameAndType(userName, type);
    }

}