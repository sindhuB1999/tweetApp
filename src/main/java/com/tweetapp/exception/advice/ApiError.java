package com.tweetapp.exception.advice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ApiError {
    private String errorMessage;
    private String errorCode;
}