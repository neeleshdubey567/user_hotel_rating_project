package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.respositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //generate random userId
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        //implement RATING SERVICE CALL:using rest template
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        //get user form database with the help of the repository.
        User user=userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given Id not found on server"+ userId));
        //fetch rating of the above user from the rating services;
        //http://localhost:8083/ratings/users/ebd3d190-8b02-4007-b9af-248ee653feff
        //there are differents way to communicate with Restapi .
        //ArrayList is converted to the rating of array class..just for the informtation...
        //below we get the rating array
        //now we can converted into the list of the array...
        Rating[] ratingOfUser =  restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{}",ratingOfUser);

        //array ek class hai jo ki convert karega array into the list...
        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();

        List<Rating> ratingList= ratings.stream().map(rating -> {
            //api call to hotel service for the hotel
            //http://localhost:8082/Hotels/b602afd6-b502-443a-a0bb-1d864186ace3
            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/Hotels/"+rating.getHotelId(), Hotel.class);
            //Hotel hotel =forEntity.getBody();

            //for the feignClient below
            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            //set hotel to rating
            rating.setHotel(hotel);
            return rating;

        }).collect(Collectors.toList());
//
        user.setRatings(ratingList);
        return user;
    }
}
