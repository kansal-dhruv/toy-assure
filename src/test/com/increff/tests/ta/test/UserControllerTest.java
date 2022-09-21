package com.increff.tests.ta.test;

import com.increff.ta.commons.exception.ApiException;
import com.increff.ta.commons.model.UserForm;
import com.increff.ta.commons.model.enums.UserType;
import com.increff.ta.constants.Constants;
import com.increff.ta.controller.UserController;
import com.increff.ta.dao.UserDao;
import com.increff.ta.dto.helper.UserDtoHelper;
import com.increff.ta.pojo.UserPojo;
import com.increff.tests.ta.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserControllerTest extends AbstractUnitTest {

  @Autowired
  UserController userController;

  @Autowired
  UserDao userDao;

  @Test
  public void createUserTest(){
    UserForm userForm = new UserForm();
    userForm.setName("DhruvTest");
    userForm.setType(UserType.CLIENT);
    userController.createUser(userForm);
    UserPojo userPojo = userDao.selectByNameAndType("DhruvTest", UserType.CLIENT);
    Assert.assertEquals(userPojo.getName(), userForm.getName());
    Assert.assertEquals(userPojo.getType(), userForm.getType());
    Assert.assertNotNull(userPojo.getId());

  }

  @Test
  public void createDuplicateUserTest(){
    UserForm userForm = new UserForm();
    userForm.setName("DhruvTest");
    userForm.setType(UserType.CUSTOMER);
    userController.createUser(userForm);
    try {
      userController.createUser(userForm);
    } catch (ApiException e){
      Assert.assertEquals(e.getCode(), Constants.CODE_USERNAME_ALREADY_IN_USE);
    }
  }

  @Test
  public void UserDtoHelperTest(){
    UserForm userForm = new UserForm();
    userForm.setName("Dhruv");
    userForm.setType(UserType.CLIENT);
    UserPojo userPojo = UserDtoHelper.convertFormToPojo(userForm);
    Assert.assertEquals(userPojo.getName(), userForm.getName());
    Assert.assertEquals(userPojo.getType(), userForm.getType());
  }


}