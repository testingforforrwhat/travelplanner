package com.test.travelplanner.destination;

import com.test.travelplanner.destination.model.DestinationDto;
import com.test.travelplanner.destination.model.DestinationEntity;
import com.test.travelplanner.destination.model.AttractionDto;
import com.test.travelplanner.destination.model.AttractionEntity;
import com.test.travelplanner.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

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
        // You can add logic to handle attractions if necessary
        return entity;
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

