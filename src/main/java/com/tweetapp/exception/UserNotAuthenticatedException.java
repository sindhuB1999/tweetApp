package com.tweetapp.exception;

public class UserNotAuthenticatedException extends RuntimeException {
    private static final long serialVersionUID = -942431621801412878L;

    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
