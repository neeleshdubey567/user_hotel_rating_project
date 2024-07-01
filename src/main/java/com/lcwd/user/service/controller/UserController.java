package com.lcwd.user.service.controller;

import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userservice;

    //create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = userservice.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    Logger logger= LoggerFactory.getLogger(UserController.class);

    // get single user

    int retryCount=1;
    @GetMapping("/{userId}")
    //@CircuitBreaker(name="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //@Retry(name="ratingHotelService" , fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name="userRateLimiter", fallbackMethod="ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        logger.info("get single user Handler: UserController");
        logger.info("Retry Count: {}",retryCount);
        retryCount++;
        User user = userservice.getUser(userId);
        return ResponseEntity.ok(user);
    }




    //creating fallback method for the circuit Breaker
    //if any service rating or hotel get down or not working then this fallback will be executed.
    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
        logger.info("FallMethod Executed Because other Rating is down!! ",ex.getMessage());
        User user=User.builder()
                .name("Dummy")
                .email("Dummy@gmail.com")
                .about("This Dummy PROFILE").build();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }



    //get all user
    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUsers = userservice.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    //to delete the user for the data Bases;
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<User> deleteSingleUser(@PathVariable String userId){
//        return ResponseEntity.ok(allUsers);;
//    }   to be continue....


}

