package com.increff.ta.controller.handler;

import com.increff.ta.service.ApiException;
import org.springframework.http.HttpHeaders;
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
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return handleExceptionInternal(ex, "Internal Server error",
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
