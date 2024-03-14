package com.effortless.effortlessmarket.global.exception;

import org.springframework.http.HttpStatus;

public class CustomExceptionType extends RuntimeException {

    private final CustomException customException;

    public CustomExceptionType(CustomException customException){
        this.customException = customException;

    }


    public HttpStatus getHttpStatus() {
        return customException.getHttpStatus();
    }

    public String getMessage(){
        return customException.getMessage();
    }
}
