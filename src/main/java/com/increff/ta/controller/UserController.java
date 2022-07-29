package com.increff.ta.controller;

import com.increff.ta.model.UserForm;
import com.increff.ta.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Used to create new users")
    @RequestMapping(value = "api/user/create", method = RequestMethod.POST)
    public String createUser(@RequestBody UserForm userData) {
        userService.createUser(userData);
        return "Success";
    }
}
