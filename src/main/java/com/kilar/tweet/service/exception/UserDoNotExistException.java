package com.kilar.tweet.service.exception;

public class UserDoNotExistException extends RuntimeException {
    public UserDoNotExistException(String message) {
        super(message);
    }
}
