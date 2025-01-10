package com.test.travelplanner.repository;

import com.test.travelplanner.model.entity.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    // Find a destination by name (optional but useful if names are unique or relevant)
    Optional<DestinationEntity> findByName(String name);

    // Find all destinations by location
    List<DestinationEntity> findByLocation(String location);

    // Find destinations by name and location (for cases with non-unique names)
    Optional<DestinationEntity> findByNameAndLocation(String name, String location);
}
