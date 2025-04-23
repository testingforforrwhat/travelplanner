package com.test.travelplanner.controller;

import com.test.travelplanner.model.dto.AttractionDto;
import com.test.travelplanner.model.dto.DestinationDto;
import com.test.travelplanner.service.DestinationService;
import com.test.travelplanner.service.AttractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {

    private final DestinationService destinationService;
    private final AttractionService attractionService;

    public DestinationController(DestinationService destinationService, AttractionService attractionService) {
        this.destinationService = destinationService;
        this.attractionService = attractionService;
    }

    // Fetch all destinations
    @GetMapping
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        List<DestinationDto> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    // Fetch a destination by ID
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) {
        Optional<DestinationDto> destination = destinationService.getDestinationById(id);
        return destination.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Create or update a destination
    @PostMapping
    public ResponseEntity<DestinationDto> saveDestination(@RequestBody DestinationDto destinationDto) {
        DestinationDto savedDestination = destinationService.saveOrUpdateDestination(destinationDto);
        return ResponseEntity.ok(savedDestination);
    }

    // Delete a destination by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch attractions for a specific destination
    @GetMapping("/{destinationId}/attractions")
    public ResponseEntity<List<AttractionDto>> getAttractionsByDestination(@PathVariable Long destinationId) {
        try {
            List<AttractionDto> attractions = destinationService.getAttractionsForDestination(destinationId);
            return ResponseEntity.ok(attractions);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

