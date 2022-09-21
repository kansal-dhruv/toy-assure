package com.increff.ta.dto;

import com.increff.ta.api.UserApi;
import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.UserForm;
import com.increff.ta.constants.Constants;
import com.increff.ta.dto.helper.UserDtoHelper;
import com.increff.ta.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDto {

  private final UserApi userApi;

  @Autowired
  public UserDto(UserApi userApi) {
    this.userApi = userApi;
  }

  public void createUser(UserForm userForm) {
    UserPojo userPojo = UserDtoHelper.convertFormToPojo(userForm);
    UserPojo user = userApi.getUserByNameAndType(userPojo.getName(), userPojo.getType());
    if (Objects.nonNull(user)) {
      throw new ApiException(Constants.CODE_USERNAME_ALREADY_IN_USE, Constants.MSG_USERNAME_ALREADY_EXISTS);
    }
    userApi.createUser(userPojo);
  }
}