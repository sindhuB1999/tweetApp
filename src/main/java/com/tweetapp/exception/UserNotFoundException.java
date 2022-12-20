package com.tweetapp.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -942431621801412878L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
