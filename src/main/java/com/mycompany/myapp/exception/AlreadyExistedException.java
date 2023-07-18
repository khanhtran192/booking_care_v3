package com.mycompany.myapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AlreadyExistedException extends RuntimeException {

    private int code;
    private String message;

    public AlreadyExistedException(String message) {
        super(message);
        this.code = HttpStatus.ALREADY_REPORTED.value();
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
