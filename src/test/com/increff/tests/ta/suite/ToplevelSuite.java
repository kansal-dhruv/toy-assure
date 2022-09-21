package com.increff.tests.ta.suite;

import com.increff.tests.ta.test.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({UserControllerTest.class, ProductControllerTest.class, BinControllerTest.class,
    ChannelControllerTest.class, OrderControllerCSVTest.class, OrderControllerFormTest.class})
@RunWith(Suite.class)
public class ToplevelSuite {
}