package com.mycompany.myapp.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    private int code;
    private String message;

    public NotFoundException(String message) {
        super(message);
        this.code = HttpStatus.NOT_FOUND.value();
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
