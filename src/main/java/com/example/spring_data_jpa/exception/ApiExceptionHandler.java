package com.example.spring_data_jpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    private ResponseEntity<ApiError> handleDuplicateResourceException(DuplicateResourceException dre) {
        ApiError apiError = new ApiError(dre.getMessage(), HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
