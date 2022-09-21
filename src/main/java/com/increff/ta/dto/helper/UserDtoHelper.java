package com.increff.ta.dto.helper;

import com.increff.ta.commons.model.UserForm;
import com.increff.ta.pojo.UserPojo;

public class UserDtoHelper {
  public static UserPojo convertFormToPojo(UserForm userForm) {
    UserPojo userPojo = new UserPojo();
    userPojo.setName(userForm.getName());
    userPojo.setType(userForm.getType());
    return userPojo;
  }
}