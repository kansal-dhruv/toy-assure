package com.increff.ta.controller.handler;

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

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(Object response, HttpStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", response);
        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> successResponse() {
        return generateResponse("Success", HttpStatus.OK);
    }
}
