package com.increff.ta.dto;

import com.increff.ta.api.ApiException;
import com.increff.ta.api.UserApi;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.UserDtoHelper;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDto {

    @Autowired
    private UserApi userApi;

    public void createUser(UserForm userForm){
        User userPojo = UserDtoHelper.convertFormToPojo(userForm);
        User user = userApi.getUserByNameAndType(userPojo.getName(), userPojo.getType());
        if (user != null) {
            throw new ApiException(Constants.CODE_USERNAME_ALREADY_IN_USE, Constants.MSG_USERNAME_ALREADY_EXISTS);
        }
        userApi.createUser(userPojo);
    }
}