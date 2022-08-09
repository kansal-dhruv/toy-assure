package com.increff.ta.controller.handler;

import com.increff.ta.service.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {ApiException.class})
    protected ResponseEntity<Object> handleConflictApiException(
            RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseHandler.generateResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,null);
    }

    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseHandler.generateResponse("Internal Server Error - Please contact system Administrator.",HttpStatus.INTERNAL_SERVER_ERROR,null);
    }
}
