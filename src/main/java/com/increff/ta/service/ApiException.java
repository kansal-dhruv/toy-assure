package com.increff.ta.service;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ApiException(String string) {
		super(string);
	}

}
