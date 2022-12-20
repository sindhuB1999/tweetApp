package com.tweetapp.service;

import com.tweetapp.exception.UserAlreadyExistsException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MongoOperations mongoOperations;

    private static final String EMAIL = "email";

    @Autowired
    public UserService(UserRepository userRepository, MongoOperations mongoOperations) {
        this.userRepository = userRepository;
        this.mongoOperations = mongoOperations;
    }

    public String saveUser(User user) {
        log.info("UserService >> saveUser >> user:{}", user);
        User verifyUser = getUser(user);
        if (verifyUser != null) {
            log.error("UserService >> saveUser >> UserAlreadyExistsException");
            throw new UserAlreadyExistsException("User already exists with email");
        }
        userRepository.save(user);
        log.info("UserService >> saveUser >> user saved successfully");
        return "User registered successfully!! ";
    }

    private User getUser(User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL).is(user.getEmail()));
        return mongoOperations.findOne(query, User.class);
    }


    public String signUp(String email, String password) {
        log.info("UserService >> signUp >> user:{}", email);
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL).is(email).and("password").is(password));
        User user = mongoOperations.findOne(query, User.class);
        if (user == null) {
            log.error("UserService >> signUp >> UserNotFoundException");
            throw new UserNotFoundException("User doesn't exists!!");
        }
        updateIsActive(query, user);
        return "User logged in successfully";
    }

    private void updateIsActive(Query query, User user) {
        user.setIsActive(true);
        Update update = new Update();
        update.set("isActive", true);
        mongoOperations.updateFirst(query, update, User.class);
    }

    public String forgotPassword(String username, String password) {
        log.info("UserService >> forgotPassword >> for email {}", username);
        User existingUser = userRepository.findByEmail(username);
        if (existingUser == null) {
            log.error("UserService >> forgotPassword >> UserNotFoundException");
            throw new UserNotFoundException("Invalid username " + username);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where(EMAIL).is(username));
        Update update = new Update();
        update.set("password", password);
        mongoOperations.updateFirst(query, update, User.class);
        return "Password change success!!";
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.error("UserService >> getAllUser >> UsersNotExistException");
            throw new UserNotFoundException("Users doesn't exists!!");
        }
        log.info("UserService >> getAllUser >> fetched user list:{}", users);
        return users;
    }

    public User getUserByUserName(String firstName) {
        log.info("UserService >> getUserByUserName >> firstName:{}",firstName);
        Optional<User> user = userRepository.findByFirstName(firstName);
        if (!user.isPresent()) {
            log.error("UserService >> getUserByUserName >> UserNotFoundException");
            throw new UserNotFoundException("User is not found!!");
        }
        log.info("UserService >> getUserByUserName >> user:{}", user);
        return user.get();
    }
}