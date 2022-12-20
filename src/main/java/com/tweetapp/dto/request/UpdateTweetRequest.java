package com.tweetapp.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
public class UpdateTweetRequest {
    @NotBlank
    private String text;

}