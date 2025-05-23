package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.trip.Trip;
import com.test.travelplanner.model.entity.trip.TripHighlight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripHighlightRepository extends JpaRepository<TripHighlight, Long> {

}