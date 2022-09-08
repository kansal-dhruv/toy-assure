package com.increff.ta.model;

import com.increff.ta.enums.UserType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserForm {

    @NotBlank
    private String name;

    @NotNull(message = "can be either CLIENT/CUSTOMER")
    private UserType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}