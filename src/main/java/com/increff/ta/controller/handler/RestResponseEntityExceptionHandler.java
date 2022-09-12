package com.increff.ta.controller.handler;

import com.increff.ta.api.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {ApiException.class})
    protected ResponseEntity<Object> handleConflictApiException(
            ApiException ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseHandler.generateResponse(ex, HttpStatus.BAD_REQUEST, ex.getDescription());
    }

    @ExceptionHandler(value
            = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleConflictApiException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String errorMessage = "Field '" + ex.getName() + "' must be a " + ex.getRequiredType().getSimpleName();
        return ResponseHandler.generateResponse(new ApiException(-1, "Bad Input"), HttpStatus.BAD_REQUEST, errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map( error -> "Field '" + error.getField() + "' " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseHandler.generateResponse(new ApiException(-1, "Bad Input"), HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(value
            = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return ResponseHandler.generateResponse(new ApiException(-1, "Bad Input"), HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(value
            = {RuntimeException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseHandler.generateResponse("Internal Server Error - Please contact system Administrator.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(value
        = {Exception.class})
    protected ResponseEntity<Object> handleConflict(
        Exception ex, WebRequest request) {
        ex.printStackTrace();
        return ResponseHandler.generateResponse("Internal Server Error - Please contact system Administrator.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }



}