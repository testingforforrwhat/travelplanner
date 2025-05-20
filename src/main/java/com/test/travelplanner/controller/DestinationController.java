package com.test.travelplanner.controller;

import com.test.travelplanner.annotation.RedisCache;
import com.test.travelplanner.model.dto.AttractionDto;
import com.test.travelplanner.model.dto.DestinationDto;
import com.test.travelplanner.model.dto.unifiedGlobalResponse.ApiResponse;
import com.test.travelplanner.model.entity.DestinationEntity;
import com.test.travelplanner.repository.DestinationRepository;
import com.test.travelplanner.service.impl.DestinationService;
import com.test.travelplanner.service.impl.AttractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/destinations")
@CrossOrigin
public class DestinationController {

    private final DestinationService destinationService;
    private final AttractionService attractionService;
    private final DestinationRepository destinationRepository;

    public DestinationController(DestinationService destinationService, AttractionService attractionService, DestinationRepository destinationRepository) {
        this.destinationService = destinationService;
        this.attractionService = attractionService;
        this.destinationRepository = destinationRepository;
    }

    // Fetch all destinations
    @ResponseBody
    @GetMapping
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        List<DestinationDto> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    // Fetch a destination by ID
    @RedisCache( duration = 60 * 60 )
    @ResponseBody
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

    @PostMapping("/createListingDestinations")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Optional<DestinationEntity>>> createListing(
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        destinationService.createListing(
                name,
                location,
                description,
                image);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(destinationRepository.findByName(name)));
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

