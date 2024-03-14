package com.effortless.effortlessmarket.global.exception;

import lombok.Data;

@Data
public class CustomResponse {
    private boolean success;
    private String message;
    private Object data;

    public CustomResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
