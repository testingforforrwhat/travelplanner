package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.TripEntity;
import com.test.travelplanner.model.entity.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

}