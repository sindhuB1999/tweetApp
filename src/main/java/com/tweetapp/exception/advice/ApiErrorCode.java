package com.tweetapp.exception.advice;


import com.fasterxml.jackson.annotation.JsonValue;

public enum ApiErrorCode {
    USER_ALREADY_EXISTS("TWEET_APP_ERROR_0001"),
    USER_NOT_FOUND("TWEET_APP_ERROR_0002"),
    TOKEN_ERROR("TWEET_APP_ERROR_0003"),
    TWEET_EMPTY_ERROR("TWEET_APP_ERROR_0004"),
    INVALID_TWEET_ERROR("TWEET_APP_ERROR_0005"),
    LIKE_CONFLICT_ERROR("TWEET_APP_ERROR_0006");

    private String errorCode;

    private ApiErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public String getString() {
        return this.errorCode;
    }

}


