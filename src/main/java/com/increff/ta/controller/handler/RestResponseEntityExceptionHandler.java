package com.increff.ta.controller.handler;

import com.increff.ta.service.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getDescription());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseHandler.generateResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value
            = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations().stream().map( constraintViolation ->  constraintViolation.getMessage()).collect(Collectors.toList());
        return ResponseHandler.generateResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value
            = {RuntimeException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        return ResponseHandler.generateResponse("Internal Server Error - Please contact system Administrator.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }


}
