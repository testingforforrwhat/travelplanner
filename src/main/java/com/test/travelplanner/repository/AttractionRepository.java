package com.test.travelplanner.repository;

import com.test.travelplanner.destination.model.AttractionEntity;
import com.test.travelplanner.destination.model.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<AttractionEntity, Long> {

    // Find all attractions for a specific destination
    List<AttractionEntity> findByDestination(DestinationEntity destination);

    // Find an attraction by name within a specific destination
    Optional<AttractionEntity> findByNameAndDestination(String name, DestinationEntity destination);
}
