package com.mycompany.myapp.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {

    private int code;
    private String message;

    public BadRequestException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST.value();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
