package com.ltd.abctelecom.exception;

import com.ltd.abctelecom.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    ResponseEntity<ExceptionResponse> CustomExceptionHandler(CustomException e){
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .errorCode(e.getErrorCode())
                        .build(),
                HttpStatus.NOT_FOUND);

    }
}
