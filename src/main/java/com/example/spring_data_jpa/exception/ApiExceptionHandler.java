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

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException se) {
        ApiError apiError = new ApiError(se.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException iae) {
        ApiError apiError = new ApiError(iae.getMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StatusConflictException.class)
    private ResponseEntity<ApiError> handleStatusConflictException(StatusConflictException sc) {
        ApiError apiError = new ApiError(sc.getMessage(), HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
