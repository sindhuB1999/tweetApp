package com.tweetapp.controller;

import com.tweetapp.model.User;
import com.tweetapp.service.UserService;
import com.tweetapp.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "User register,login,fetching details operations")
@RequestMapping("/api/v1.0/tweets")
public class UserController {

    private final UserService userService;
    private final Auth auth;

    @Autowired
    public UserController(UserService userService, Auth auth) {
        this.userService = userService;
        this.auth = auth;
    }

    @ApiOperation(value = "register as new user", response = String.class)
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        log.info("UserController >> register >> user Details:{}", user);
        String response = userService.saveUser(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "login", response = String.class)
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        log.info("UserController >> login >> email:{}", email);
        String response = userService.signUp(email, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "forgot Password", response = String.class)
    @GetMapping("/{username}/forgot")
    public ResponseEntity<String> forgotPwd(@PathVariable("username") String username, @RequestParam String password) {
        log.info("UserController >> forgotPwd >> username:{}", username);
        String response = userService.forgotPassword(username, password);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "get all users", response = List.class)
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("auth-token") String token) {
        log.info("UserController >> getAllUsers");
        auth.validateUser(token);
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "search by username", response = User.class)
    @GetMapping("/user/search/{username}")
    public ResponseEntity<User> searchByUserName(@RequestHeader("auth-token") String token, @PathVariable("username") String firstName) {
        log.info("UserController >> searchByUserName:{}", firstName);
        auth.validateUser(token);
        User user = userService.getUserByUserName(firstName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}