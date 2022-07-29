package com.increff.ta.model;

import com.increff.ta.enums.UserType;

public class UserForm {
    private String name;
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
