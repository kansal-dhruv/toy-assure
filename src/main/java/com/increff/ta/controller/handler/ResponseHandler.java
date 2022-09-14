package com.increff.ta.controller.handler;

import com.increff.ta.api.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (message != null)
            map.put("message", message);
        if (responseObj != null)
            map.put("data", responseObj);
        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(ApiException e, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(e.getCode() != null)
            map.put("code", e.getCode());
        if (e.getMessage() != null)
            map.put("message", e.getMessage());
        if (responseObj != null)
            map.put("data", responseObj);
        return new ResponseEntity<Object>(map, status);
    }


    public static ResponseEntity<Object> successResponse() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "Success");
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }
}