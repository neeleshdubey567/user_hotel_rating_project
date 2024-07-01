package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;

public interface UserService {
    //user operations

    //to create
    User saveUser(User user);

    //to getAllUsers
    List<User> getAllUsers();

    //to getUser by using the singleUserId
    User getUser(String userId);

    //to delete using given userId


    //to delete AllUserId

}
