package com.tweetapp.exception;

public class TweetEmptyException extends RuntimeException {
    private static final long serialVersionUID = -942431621801412878L;
    public TweetEmptyException(String message) {
        super(message);
    }
}

