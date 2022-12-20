package com.tweetapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tweets")
public class Tweet {
    @Id
    private String id;
    private String userEmail;
    private String userName;
    private Date timestamp = new Date();
    @NotBlank
    private String text;

    private int likes = 0;
    private List<String> likedUsers;
}