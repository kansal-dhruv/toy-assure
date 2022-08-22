//package com.increff.ta.service;
//
//import com.increff.ta.pojo.EmployeePojo;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.assertEquals;
//
//public class EmployeeServiceTest extends AbstractUnitTest {
//
//	@Autowired
//	private EmployeeService service;
//
//	@Test
//	public void testAdd() {
//		EmployeePojo p = new EmployeePojo();
//		p.setName(" Romil Jain ");
//		service.add(p);
//	}
//
//	@Test
//	public void testNormalize() {
//		EmployeePojo p = new EmployeePojo();
//		p.setName(" Romil Jain ");
//		EmployeeService.normalize(p);
//		assertEquals("romil jain", p.getName());
//	}
//
//}
