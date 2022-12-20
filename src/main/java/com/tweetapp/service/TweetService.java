package com.tweetapp.service;

import com.tweetapp.dto.request.AddTweetRequest;
import com.tweetapp.dto.request.UpdateTweetRequest;
import com.tweetapp.exception.InvalidTweetRequestException;
import com.tweetapp.exception.LikeConflictException;
import com.tweetapp.exception.TweetEmptyException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "user not found";

    @Autowired
    public TweetService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    public Tweet postTweet(AddTweetRequest tweetRequest, String username) {
        log.info("TweetService >> postTweet >> tweet:{}", tweetRequest);
        User existingUser = validateTweetRequest(tweetRequest, username);
        Tweet tweetPosted = tweetRepository.save(
                Tweet.builder().text(tweetRequest.getText())
                        .userEmail(username)
                        .userName(existingUser.getFirstName() + " " + existingUser.getLastName())
                        .likes(0)
                        .timestamp(new Date())
                        .build());
        updateUser(existingUser, tweetPosted);
        return tweetPosted;
    }


    @NotNull
    private User validateTweetRequest(AddTweetRequest tweetRequest, String email) {
        if (tweetRequest == null || tweetRequest.getText() == null) {
            log.error("TweetService >> postTweet >> TweetEmptyException");
            throw new TweetEmptyException("Tweet is empty!!");
        }
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            log.error("TweetService >> postTweet >> UserNotFoundException");
            throw new UserNotFoundException("Not a valid user");
        }
        return existingUser;
    }

    private void updateUser(User existingUser, Tweet tweetPosted) {
        if (existingUser.getTweets() == null || existingUser.getTweets().isEmpty()) {
            existingUser.setTweets(new ArrayList<>());
        }
        existingUser.getTweets().add(tweetPosted);
        userRepository.save(existingUser);
    }

    public List<Tweet> getAllTweets() {
        log.info("TweetService >> getAllTweets");
        return tweetRepository.findAll().stream().sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())).collect(Collectors.toList());
    }

    public List<Tweet> getAllTweetsByUser(String username) {
        log.info("TweetService >> getAllTweetsByUser >> username:{}",username);
        return tweetRepository.findAllByUserEmail(username);
    }

    public Tweet updateTweet(UpdateTweetRequest request, String id, String username) {
        log.info("TweetService >> updateTweet >> id:{}",id);
        if (id == null) throw new InvalidTweetRequestException("Invalid TweetRequest");
        Optional<Tweet> oldTweet = tweetRepository.findById(id);
        if (!oldTweet.isPresent()) {
            log.error("TweetService >> updateTweet >> TweetEmptyException");
            throw new TweetEmptyException("Tweet doesn't exist");
        } else if (!(oldTweet.get().getUserEmail().equals(username))) {
            throw new UserNotFoundException("not a valid user");

        }
        Tweet newTweet = Tweet.builder()
                .id(oldTweet.get().getId())
                .userEmail(oldTweet.get().getUserEmail())
                .userName(oldTweet.get().getUserName())
                .likes(oldTweet.get().getLikes())
                .timestamp(oldTweet.get().getTimestamp())
                .text(oldTweet.get().getText())
                .build();
        if (request.getText() != null) {
            newTweet.setText(request.getText());
        }
        tweetRepository.save(newTweet);


        String email = oldTweet.get().getUserEmail();
        User user = userRepository.findByEmail(email);
        Tweet tweetInUser = user.getTweets().stream().filter(t -> Objects.equals(t.getId(), id)).findAny().get();
        tweetInUser.setText(request.getText());
        userRepository.save(user);
        log.info("TweetService >> updateTweet >> tweet:{}",newTweet);
        return newTweet;
    }


    public List<Tweet> deleteTweet(String id, String username) {
        log.info("TweetService >> deleteTweet >> id:{}",id);
        if (!tweetRepository.findById(id).isPresent()) {
            log.error("TweetService >> deleteTweet >> id:{}",id);
            throw new TweetEmptyException("Tweet not found!!");
        }
        User user = userRepository.findByEmail(username);
        if (user == null) {
            log.info("TweetService >> deleteTweet >> id:{}",id);
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        List<Tweet> deletedTweet = user.getTweets().stream().filter(t -> (Objects.equals(t.getId(), id))).collect(Collectors.toList());
        tweetRepository.deleteById(id);
        user.getTweets().removeAll(deletedTweet);

        userRepository.save(user);
        List<Tweet> tweetsAfterDel = tweetRepository.findAllByUserEmail(username);
        log.info("TweetService >> deleteTweet >> after deleting tweet other tweets are:{}",tweetsAfterDel);
        return tweetsAfterDel;
    }

    public Tweet likeTweet(String id, String username) {
        log.info("TweetService >>  likeTweet >> id:{}", id);
        User user = userRepository.findByEmail(username);
        if (null == user)
            throw new UserNotFoundException(USER_NOT_FOUND);
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if (!tweet.isPresent()) throw new TweetEmptyException("Tweet not exists");
        if (tweet.get().getLikedUsers() == null || tweet.get().getLikedUsers().isEmpty()) {
            tweet.get().setLikedUsers(Collections.singletonList(username));
        } else {
            if (!tweet.get().getLikedUsers().contains(username)) {
                tweet.get().getLikedUsers().add(username);
            } else throw new LikeConflictException("like conflict error");
        }
        int updateLikeCount = tweet.get().getLikes() + 1;
        tweet.get().setLikes(updateLikeCount);
        tweetRepository.save(tweet.get());

        user.getTweets().stream().filter(t -> Objects.equals(t.getId(), id)).findFirst().get().setLikes(updateLikeCount);
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            log.error("TweetService >> likeTweet >> UserNotFoundException");
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        userRepository.save(user);
        log.info("TweetService >> likeTweet >> liked tweet");
        return tweet.get();
    }

}