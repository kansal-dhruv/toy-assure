package com.increff.tests.ta;

import com.increff.ta.api.UserApi;
import com.increff.ta.pojo.UserPojo;
import com.increff.tests.ta.config.QaConfig;
import com.increff.tests.ta.utils.TestUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AbstractUnitTest {

  protected static UserPojo client = null;

  protected static UserPojo customer = null;

  protected static Boolean isInitCalled = false;

  @Autowired
  private UserApi userApi;

  @Before
  public void init(){
    if(!isInitCalled){
      client = userApi.createUser(TestUtils.createClientUser());
      customer = userApi.createUser(TestUtils.createCustomerUser());
      isInitCalled = true;
    };
  }

}