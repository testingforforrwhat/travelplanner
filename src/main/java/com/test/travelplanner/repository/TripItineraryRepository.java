package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.trip.TripItinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripItineraryRepository extends JpaRepository<TripItinerary, Long> {

}