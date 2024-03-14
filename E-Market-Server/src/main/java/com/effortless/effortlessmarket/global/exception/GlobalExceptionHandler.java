package com.effortless.effortlessmarket.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExceptionType.class)
    public ResponseEntity<CustomResponse> handleCustomException(CustomExceptionType e) {
        CustomResponse response = new CustomResponse(false, e.getMessage(),null);
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

}
