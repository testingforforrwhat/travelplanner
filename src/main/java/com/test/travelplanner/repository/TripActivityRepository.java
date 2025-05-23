package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.trip.TripActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripActivityRepository extends JpaRepository<TripActivity, Long> {

}