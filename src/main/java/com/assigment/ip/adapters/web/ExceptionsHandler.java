package com.assigment.ip.adapters.web;

import com.assigment.ip.application.service.EmptyResultException;
import com.assigment.ip.application.service.IncorrectIpFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(EmptyResultException.class)
    public ResponseEntity<String> handleEmptyResultException(EmptyResultException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IncorrectIpFormatException.class)
    public ResponseEntity<String> handleIncorrectIpFormatException(IncorrectIpFormatException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
