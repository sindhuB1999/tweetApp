package com.tweetapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tweetapp.config.KafkaProducerConfig;
import com.tweetapp.dto.request.AddTweetRequest;
import com.tweetapp.dto.request.UpdateTweetRequest;
import com.tweetapp.dto.response.KafkaTweetAppResponse;
import com.tweetapp.model.Tweet;
import com.tweetapp.service.TweetService;
import com.tweetapp.util.Auth;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

    private final TweetService tweetService;
    private final Auth auth;
    private final KafkaProducerConfig kafkaTemplate;

    @Autowired
    public TweetController(TweetService tweetService, Auth auth, KafkaProducerConfig kafkaTemplate) {
        this.tweetService = tweetService;
        this.auth = auth;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/{username}/add")
    @ApiOperation(value = "post new tweet", response = String.class)
    public ResponseEntity<String> postTweet(@RequestHeader("auth-token") String token, @Valid @RequestBody AddTweetRequest addTweetRequest, @PathVariable("username") String username) throws JsonProcessingException {
        log.info("TweetController >> postTweet >> username:{}", username);
        auth.validateUser(token);
        Tweet tweet = tweetService.postTweet(addTweetRequest, username);
        KafkaTweetAppResponse<Tweet> tweetResponse = KafkaTweetAppResponse.ok(tweet, new Object() {
        }.getClass().getEnclosingMethod().getName());
       // publishMessageToTopic(tweetResponse);
        log.info("TweetController >> postTweet >> tweet:{}", tweetResponse);
        return new ResponseEntity<>("Posted tweet successfully", HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "get all tweets", response = KafkaTweetAppResponse.class)
    public KafkaTweetAppResponse<Tweet> loadAllTweets(@RequestHeader("auth-token") String token) throws JsonProcessingException {
        log.info("TweetController >> loadAllTweets ");
        auth.validateUser(token);
        List<Tweet> allTweets = tweetService.getAllTweets();
        KafkaTweetAppResponse<Tweet> tweetResponse = KafkaTweetAppResponse.ok(allTweets, new Object() {
        }.getClass().getEnclosingMethod().getName());
       // publishMessageToTopic(tweetResponse);
        log.info("TweetController >> loadAllTweets >> tweet:{}", tweetResponse);
        return tweetResponse;
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "get all tweets of user", response = KafkaTweetAppResponse.class)
    public KafkaTweetAppResponse<Tweet> getTweetsByUser(@RequestHeader("auth-token") String authHeader, @PathVariable("username") String username) {
        log.info("TweetController >> getTweetsByUser >> username:{}", username);
        auth.validateUser(authHeader);
        List<Tweet> allTweets = tweetService.getAllTweetsByUser(username);
        KafkaTweetAppResponse<Tweet> tweetResponse = KafkaTweetAppResponse.ok(allTweets, new Object() {
        }.getClass().getEnclosingMethod().getName());
        log.info("TweetController >> getTweetsByUser >> tweets:{}", tweetResponse);
        return tweetResponse;
    }

    @PutMapping("/{username}/update/{id}")
    @ApiOperation(value = "update tweet by id", response = String.class)
    public ResponseEntity<String> updateTweet(@RequestHeader(("auth-token")) String authToken, @RequestBody UpdateTweetRequest request, @PathVariable("username") String username, @PathVariable("id") String id) throws JsonProcessingException {
        log.info("TweetController >> updateTweet >> username:{}", username);
        auth.validateUser(authToken);
        Tweet updatedTweet = tweetService.updateTweet(request, id, username);
        KafkaTweetAppResponse<Tweet> tweetResponse = KafkaTweetAppResponse.ok(updatedTweet, new Object() {
        }.getClass().getEnclosingMethod().getName());
      //  publishMessageToTopic(tweetResponse);
        log.info("TweetController >> updateTweet >> tweet:{}", tweetResponse);
        return new ResponseEntity<>("Updated tweet successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{username}/delete/{id}")
    @ApiOperation(value = "delete tweet by username and id", response = String.class)
    public ResponseEntity<String> deleteTweet(@RequestHeader("auth-token") String authToken, @PathVariable("username") String username, @PathVariable("id") String id) throws JsonProcessingException {
        log.info("TweetController >> deleteTweet >> username:{}", username);
        auth.validateUser(authToken);
        List<Tweet> tweetsAfterDelete = tweetService.deleteTweet(id, username);
        KafkaTweetAppResponse<Tweet> tweetResponse = KafkaTweetAppResponse.ok(tweetsAfterDelete, new Object() {
        }.getClass().getEnclosingMethod().getName());
        //publishMessageToTopic(tweetResponse);
        log.info("TweetController >> deleteTweet >> tweet:{]", tweetResponse);
        return new ResponseEntity<>("Deleted tweet successfully", HttpStatus.OK);
    }

    @GetMapping("/{username}/like/{id}")
    @ApiOperation(httpMethod = "GET", value = "Like a tweet", response = KafkaTweetAppResponse.class)
    public KafkaTweetAppResponse<Tweet> like(@RequestHeader("auth-token") String token, @PathVariable("username") String username, @PathVariable("id") String id) throws JsonProcessingException {
        log.info("TweetController >> like >> username:{}", username);
        auth.validateUser(token);
        Tweet tweet = tweetService.likeTweet(id, username);
        KafkaTweetAppResponse<Tweet> response = KafkaTweetAppResponse.ok(tweet, new Object() {
        }.getClass().getEnclosingMethod().getName());
       // publishMessageToTopic(response);
        log.info("TweetController >> like >> tweet:{}", response);
        return response;
    }

    private void publishMessageToTopic(KafkaTweetAppResponse<Tweet> tweet) throws JsonProcessingException {
        log.info("TweetController >> publishMessageToTopic >> tweet");
        kafkaTemplate.send(tweet);
    }
}