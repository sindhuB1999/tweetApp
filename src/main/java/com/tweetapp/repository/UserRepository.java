package com.tweetapp.repository;

import com.tweetapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
     User findByEmail(String email) ;

    Optional<User> findByFirstName(String firstName);
}