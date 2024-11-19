package com.test.travelplanner.trip;

import com.test.travelplanner.trip.model.TripDto;
import com.test.travelplanner.trip.model.TripEntity;
import com.test.travelplanner.repository.TripRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;


import java.time.LocalDate;
import java.util.List;
@Service
public class TripService {
    private final TripRepository tripRepository;


    public TripService(
            TripRepository tripRepository
    ) {
        this.tripRepository = tripRepository;
    }


    public List<TripDto> findTripsByUserId(long userId) {
        return tripRepository.findAllByUserId(userId)
                .stream()
                .map(TripDto::new)
                .toList();
    }

    public void createTrip(long userId, long destinationId, String title, Integer days, String startDate, String status, String notes, LocalDate createdAt, LocalDate updatedAt) {
        tripRepository.save(new TripEntity(null, userId, destinationId, title, days, startDate, status, notes, createdAt, updatedAt));
    }


    public void deleteTrip(long tripId) {
        TripEntity trip = tripRepository.getReferenceById(tripId);
        tripRepository.deleteById(tripId);
    }

// update current trip??
}
