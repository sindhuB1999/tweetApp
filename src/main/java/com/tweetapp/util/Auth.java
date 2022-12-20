package com.tweetapp.util;

import com.tweetapp.exception.UserNotAuthenticatedException;
import com.tweetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Auth {

    @Autowired
    private UserRepository userRepository;

    public void validateUser(String token) {
        if (token == null)
            throw new UserNotAuthenticatedException("login error");
        if (userRepository.findByEmail(token) == null)
            throw new UserNotAuthenticatedException("Invalid Token");
    }
}