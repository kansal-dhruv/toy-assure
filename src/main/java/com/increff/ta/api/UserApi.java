package com.increff.ta.api;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.UserDao;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserApi {

    @Autowired
    private UserDao userDao;

    @Transactional
    public User createUser(User user) {
        if (userDao.selectByNameAndType(user.getName(), user.getType()) != null) {
            throw new ApiException(Constants.CODE_USERNAME_ALREADY_IN_USE, Constants.MSG_USERNAME_ALREADY_EXISTS);
        }
        return userDao.insert(user);
    }
}
