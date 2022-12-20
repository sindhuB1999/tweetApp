package com.tweetapp.dto.response;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KafkaTweetAppResponse<T> {

    private Integer status;
    private String method;
    private Object result;

    public static KafkaTweetAppResponse<Tweet> ok(Object tweet, String method) {
        return new KafkaTweetAppResponse<>(HttpStatus.OK.value(), method, tweet);
    }

    public static KafkaTweetAppResponse<User> ok(User user, String method) {
        return new KafkaTweetAppResponse<>(HttpStatus.OK.value(), method, user);
    }

    public static KafkaTweetAppResponse<Tweet> likeConflict() {
        return new KafkaTweetAppResponse<>(HttpStatus.CONFLICT.value(), "CONFLICT", null);
    }
}