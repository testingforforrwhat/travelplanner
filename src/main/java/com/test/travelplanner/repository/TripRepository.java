package com.test.travelplanner.repository;

import com.test.travelplanner.model.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TripRepository extends JpaRepository<TripEntity, Long> {

    List<TripEntity> findAllByUserId(long userId);

//    List<TripEntity> findAllByCityId(long cityId);
// update current trip???
}