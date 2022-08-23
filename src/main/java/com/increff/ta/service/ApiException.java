package com.increff.ta.service;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code = -1;
    private String description;

    public ApiException(String string) {
        super(string);
    }

    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(Integer code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
