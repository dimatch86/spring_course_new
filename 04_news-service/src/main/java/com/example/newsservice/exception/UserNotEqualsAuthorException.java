package com.example.newsservice.exception;

public class UserNotEqualsAuthorException extends RuntimeException {
    public UserNotEqualsAuthorException(String message) {
        super(message);
    }
}
