package com.tweetapp.exception.advice;

import com.tweetapp.exception.*;
import com.tweetapp.exception.InvalidTweetRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TweetAppExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(java.lang.Throwable ex) {
        String errorMessage = ex.getMessage();
        ApiError error = new ApiError(errorMessage, ApiErrorCode.USER_ALREADY_EXISTS.getString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(java.lang.Throwable ex) {
        String errorMessage = ex.getMessage();
        ApiError error = new ApiError(errorMessage, ApiErrorCode.USER_NOT_FOUND.getString());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<Object> handleUserNotAuthenticatedException(java.lang.Throwable ex) {
        String errorMessage = ex.getMessage();
        ApiError error = new ApiError(errorMessage, ApiErrorCode.TOKEN_ERROR.getString());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TweetEmptyException.class)
    public ResponseEntity<Object> handleTweetEmptyException(java.lang.Throwable ex) {
        String errorMessage = ex.getMessage();
        ApiError error = new ApiError(errorMessage, ApiErrorCode.TWEET_EMPTY_ERROR.getString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTweetRequestException.class)
    public ResponseEntity<Object> handleInvalidTweetRequest(java.lang.Throwable ex) {
        String errorMessage = ex.getMessage();
        ApiError error = new ApiError(errorMessage, ApiErrorCode.INVALID_TWEET_ERROR.getString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LikeConflictException.class)
    public ResponseEntity<Object> handleLikeConflictException(java.lang.Throwable ex) {
        String errorMessage = ex.getMessage();
        ApiError error = new ApiError(errorMessage, ApiErrorCode.LIKE_CONFLICT_ERROR.getString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}


