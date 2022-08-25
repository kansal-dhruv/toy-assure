package com.increff.ta.model;

import com.increff.ta.enums.UserType;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserForm {

    @NotNull(message = "User name cannot be null")
    @NotBlank(message = "User name cannot be blank")
    @SafeHtml
    private String name;

    @NotNull(message = "UserType cannot be null")
    @NotNull(message = "UserType cannot be null")
    @SafeHtml
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
