package com.lcwd.user.service.external.services;

import com.lcwd.user.service.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

    //Here we have to just declare the method we will get the implementation by spring boot at
    //at the runtime..
    //With the help of feign client we can call api [of the method which is present in
    // the HOTEL-SERVICE Microservices] of the hotel service.
    @GetMapping("/Hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") String hotelId);
}
