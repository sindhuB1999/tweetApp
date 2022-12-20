package com.tweetapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Size(min = 5,max = 10,message = "first name should be 5 to 10 characters")
    private String firstName;

    @Size(min = 5,max = 10,message = "last name should be 5 to 10 characters")
    private String lastName;

    @NotNull
    private String gender;

    @NotNull
    private String dateOfBirth;

    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotEmpty(message = "password cannot be empty")
    private String password;

    private Boolean isActive;

    private List<Tweet> tweets;

}