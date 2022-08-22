package com.increff.ta.service;

import com.increff.ta.dao.UserDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends AbstractUnitTest {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Test
    public void Test1_createUserTest() {
        UserForm userForm = new UserForm();
        userForm.setName("Dhruv");
        userForm.setType(UserType.CUSTOMER);
        User user = userService.createUser(userForm);
        Assert.assertEquals(userForm.getName(), user.getName());
        Assert.assertEquals(userForm.getType(), user.getType());
    }

    @Test(expected = ApiException.class)
    public void Test2_createUserTestWithSameName() {
        UserForm userForm = new UserForm();
        userForm.setName("Dhruv");
        userForm.setType(UserType.CLIENT);
        userService.createUser(userForm);
        userService.createUser(userForm);
        Assert.fail();
    }
}
