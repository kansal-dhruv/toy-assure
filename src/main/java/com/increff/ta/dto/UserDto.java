package com.increff.ta.dto;

import com.increff.ta.api.UserApi;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDto {

    @Autowired
    private UserApi userApi;

    public User createUser(UserForm userForm){
        User userPojo = convertFormToPojo(userForm);
        userPojo = userApi.createUser(userPojo);
        return userPojo;
    }

    private User convertFormToPojo(UserForm userForm) {
        User userPojo = new User();
        userPojo.setName(userForm.getName());
        userPojo.setType(userForm.getType());
        return userPojo;
    }
}
