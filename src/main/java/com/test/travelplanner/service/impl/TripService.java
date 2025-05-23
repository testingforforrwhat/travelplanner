package com.test.travelplanner.service.impl;

import com.test.travelplanner.model.dto.TripDto;
import com.test.travelplanner.model.entity.TripEntity;
import com.test.travelplanner.model.entity.trip.Trip;
import com.test.travelplanner.repository.TripEntityRepository;
import com.test.travelplanner.repository.TripRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;


import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final TripEntityRepository tripEntityRepository;
    private final TripRepository tripRepository;


    public TripService(
            TripEntityRepository tripEntityRepository,
            TripRepository tripRepository) {
        this.tripEntityRepository = tripEntityRepository;
        this.tripRepository = tripRepository;
    }


    public List<TripDto> findTripsByUserId(long userId) {
        return tripEntityRepository.findAllByUserId(userId)
                .stream()
                .map(TripDto::new)
                .toList();
    }

    public void createTrip(long userId, long destinationId, String title, Integer days, String startDate, String status, String notes, LocalDate createdAt, LocalDate updatedAt) {
        tripEntityRepository.save(new TripEntity(null, userId, destinationId, title, days, startDate, status, notes, createdAt, updatedAt));
    }


    public void deleteTrip(long tripId) {
        TripEntity trip = tripEntityRepository.getReferenceById(tripId);
        tripEntityRepository.deleteById(tripId);
    }

    public Optional<Trip> findTripsDataForPage(long tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        return trip;
    }

// update current trip??
}
