package com.increff.ta.dto.helper;

import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;

public class UserDtoHelper {
  public static User convertFormToPojo(UserForm userForm) {
    User userPojo = new User();
    userPojo.setName(userForm.getName());
    userPojo.setType(userForm.getType());
    return userPojo;
  }
}