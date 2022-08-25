package com.increff.ta.service;

import com.increff.ta.constants.Constants;
import com.increff.ta.dao.UserDao;
import com.increff.ta.enums.UserType;
import com.increff.ta.model.UserForm;
import com.increff.ta.pojo.User;
import com.increff.ta.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    @Test
    public void addUser() {
        when(userDao.selectByName(notNull(String.class))).thenReturn(null);
        when(userDao.insert(any(User.class))).thenReturn(TestUtils.getClientUser());
        UserForm userForm = new UserForm();
        userForm.setName("client");
        userForm.setType(UserType.CLIENT);
        Assert.assertNotNull(userService.createUser(userForm));
    }

    @Test
    public void addUserFail() {
        when(userDao.selectByName(notNull(String.class))).thenReturn(TestUtils.getClientUser());
        when(userDao.insert(any(User.class))).thenReturn(TestUtils.getClientUser());
        UserForm userForm = new UserForm();
        userForm.setName("client");
        userForm.setType(UserType.CLIENT);
        try {
            userService.createUser(userForm);
        } catch (ApiException e) {
            Assert.assertEquals(e.getCode(), Constants.CODE_USERNAME_ALREADY_IN_USE);
        }
    }
}