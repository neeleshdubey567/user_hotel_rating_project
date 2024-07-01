package com.lcwd.user.service.external.services;


import com.lcwd.user.service.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@Service
@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

    //to get


    //to post

    //now use this serivce for posting the rating value int the database.
    //call this in UserserviceImpl class for further implemtation from this api.
    //Below this is for creating rating, we are here just testing in the userServiceApplicationTests for
    // the testing we not rquire din the project.
    @PostMapping("/ratings")
    public ResponseEntity<Rating> createRating(Rating rating);

    //to put

    @PutMapping("/ratings/{ratingId}")
    public  ResponseEntity<Rating> updateRating(@PathVariable("ratingId") String ratingId, Rating rating);

    //to delete
    @DeleteMapping("/ratings/{ratingId}")
    public void deleteRating(@PathVariable("ratingId") String ratingId);
}
