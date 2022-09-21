package com.increff.ta.controller;

import com.increff.ta.commons.model.UserForm;
import com.increff.ta.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserDto userDto;

  @Autowired
  public UserController(UserDto userDto) {
    this.userDto = userDto;
  }

  @ApiOperation(value = "Used to create new users")
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public void createUser(@Valid @RequestBody UserForm userData) {
    userDto.createUser(userData);
  }
}