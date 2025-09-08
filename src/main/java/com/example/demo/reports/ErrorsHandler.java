package com.example.demo.reports;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException error) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception error) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error.getMessage());
    }
}
