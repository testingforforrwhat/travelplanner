package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.trip.Trip;
import com.test.travelplanner.model.entity.trip.TripOverview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripOverviewRepository extends JpaRepository<TripOverview, Long> {

}