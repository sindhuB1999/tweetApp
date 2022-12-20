package com.tweetapp.exception;

public class InvalidTweetRequestException extends RuntimeException {

    private static final long serialVersionUID = -942431621801412878L;

    public InvalidTweetRequestException(String message) {
        super(message);
    }
}