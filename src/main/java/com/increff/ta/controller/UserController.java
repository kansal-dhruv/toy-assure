package com.increff.ta.controller;

import com.increff.ta.controller.handler.ResponseHandler;
import com.increff.ta.model.UserForm;
import com.increff.ta.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Api
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Transactional
    @ApiOperation(value = "Used to create new users")
    @RequestMapping(value = "api/user/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserForm userData) {
        userService.createUser(userData);
        return ResponseHandler.successResponse();
    }
}
