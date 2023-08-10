package com.mycompany.myapp.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerBadRequestException(NotFoundException e) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(e.getMessage());
        messageResponse.setDate(LocalDateTime.now());
        messageResponse.setCode(e.getCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
    }

    @ExceptionHandler(AlreadyExistedException.class)
    public ResponseEntity<Object> handlerBadRequestException(AlreadyExistedException e) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(e.getMessage());
        messageResponse.setDate(LocalDateTime.now());
        messageResponse.setCode(e.getCode());
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(messageResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handlerBadRequestException(BadRequestException e) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(e.getMessage());
        messageResponse.setDate(LocalDateTime.now());
        messageResponse.setCode(e.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
    }
}
