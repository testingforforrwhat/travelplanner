package com.test.travelplanner.service;

import com.test.travelplanner.model.dto.AttractionDto;
import com.test.travelplanner.model.dto.DestinationDto;
import com.test.travelplanner.model.entity.DestinationEntity;
import com.test.travelplanner.model.entity.AttractionEntity;
import com.test.travelplanner.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    // Fetch all destinations
    public List<DestinationDto> getAllDestinations() {
        return destinationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Fetch a destination by ID
    public Optional<DestinationDto> getDestinationById(Long id) {
        return destinationRepository.findById(id).map(this::convertToDto);
    }

    // Create or update a destination
    public DestinationDto saveOrUpdateDestination(DestinationDto destinationDto) {
        DestinationEntity entity = convertToEntity(destinationDto);
        DestinationEntity savedEntity = destinationRepository.save(entity);
        return convertToDto(savedEntity);
    }

    // Delete a destination
    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }

    // Convert DestinationEntity to DestinationDto
    private DestinationDto convertToDto(DestinationEntity entity) {
        List<AttractionDto> attractionDtos = entity.getAttractions().stream()
                .map(this::convertToDto)
                .toList();

        return new DestinationDto(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getAverageRating(),
                attractionDtos
        );
    }

    // Convert AttractionEntity to AttractionDto
    private AttractionDto convertToDto(AttractionEntity entity) {
        return new AttractionDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getOpeningHours(),
                entity.getTicketPrice(),
                entity.getImageUrl(),
                entity.getDestination().getId()
        );
    }

    // Convert DestinationDto to DestinationEntity
    private DestinationEntity convertToEntity(DestinationDto dto) {
        DestinationEntity entity = new DestinationEntity(
                dto.name(),
                dto.location(),
                dto.description(),
                dto.imageUrl(),
                dto.averageRating()
        );

        // Convert attractions if present in the DTO
        if (dto.attractions() != null) {
            entity.setAttractions(
                    dto.attractions().stream()
                            .map(this::convertToEntity) // Using the AttractionDto to AttractionEntity converter
                            .peek(attraction -> attraction.setDestination(entity)) // Set the parent destination
                            .collect(Collectors.toList())
            );
        }

        return entity;
    }


    private AttractionEntity convertToEntity(AttractionDto dto) {
        return new AttractionEntity(
                dto.name(),
                dto.description(),
                dto.openingHours(),
                dto.ticketPrice(),
                dto.imageUrl(),
                null  // The destination will be set later in the parent entity
        );
    }

    public List<AttractionDto> getAttractionsForDestination(Long destinationId) {
        Optional<DestinationEntity> destinationEntity = destinationRepository.findById(destinationId);
        if (destinationEntity.isEmpty()) {
            throw new RuntimeException("Destination not found");
        }

        return destinationEntity.get().getAttractions().stream()
                .map(this::convertToDto)
                .toList();
    }

}

