package com.tweetapp.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -942431621801412878L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
