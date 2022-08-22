package com.increff.ta.service;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer code = -1;
	
	public ApiException(String string) {
		super(string);
	}

	public ApiException(Integer code, String message){
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
