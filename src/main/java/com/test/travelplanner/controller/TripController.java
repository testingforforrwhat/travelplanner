package com.test.travelplanner.controller;


import com.test.travelplanner.model.dto.TripDto;
import com.test.travelplanner.model.TripRequest;
import com.test.travelplanner.model.entity.UserEntity;
import com.test.travelplanner.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/trips")
public class TripController {


    private final TripService tripService;

// // hard code a fake userEntity when necessary
// private final UserEntity user = new UserEntity();

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }


    @GetMapping
    public List<TripDto> getTrips(@AuthenticationPrincipal UserEntity user) {
        return tripService.findTripsByUserId(user.getId());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrips(@AuthenticationPrincipal UserEntity user, @RequestBody TripRequest body) {
        tripService.createTrip(user.getId(), body.destinationId(), body.title(), body.days(), body.startDate(), body.status(), body.notes(), body.createdAt(),body.updatedAt());
    }


    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrips(@AuthenticationPrincipal UserEntity user, @PathVariable long tripId) {
        tripService.deleteTrip(tripId);
    }
}

