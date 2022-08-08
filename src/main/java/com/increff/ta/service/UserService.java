package com.increff.ta.service;

import com.increff.ta.dao.UserDao;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void createUser(UserForm userData){
        User user = convertFormToPojo(userData);
        if(userDao.selectByName(user.getName()) == null){
            throw new ApiException("Client name already in use");
        }

        userDao.insert(user);
    }

    public User convertFormToPojo(UserForm userForm){
        User userpojo = new User();
        userpojo.setName(userForm.getName());
        userpojo.setType(userForm.getType());
        return userpojo;
    }
}
