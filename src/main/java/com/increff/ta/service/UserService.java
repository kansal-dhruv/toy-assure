package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.UserDao;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User createUser(UserForm userData){
        User user = convertFormToPojo(userData);
        if(userDao.selectByName(user.getName()) != null){
            throw new ApiException(Constants.CODE_USERNAME_ALREADY_IN_USE, Constants.MSG_USERNAME_ALREADY_EXISTS);
        }
        return userDao.insert(user);
    }

    public User getUser(Long clientId){
        return userDao.selectById(clientId);
    }

    public User convertFormToPojo(UserForm userForm){
        User userPojo = new User();
        userPojo.setName(userForm.getName());
        userPojo.setType(userForm.getType());
        return userPojo;
    }
}
