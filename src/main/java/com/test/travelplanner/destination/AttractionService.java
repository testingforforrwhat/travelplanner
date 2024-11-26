package com.test.travelplanner.destination;

import com.test.travelplanner.destination.model.AttractionDto;
import com.test.travelplanner.destination.model.AttractionEntity;
import com.test.travelplanner.destination.model.DestinationEntity;
import com.test.travelplanner.repository.AttractionRepository;
import com.test.travelplanner.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    private final DestinationRepository destinationRepository;
    private final AttractionRepository attractionRepository;

    @Autowired
    public AttractionService(DestinationRepository destinationRepository, AttractionRepository attractionRepository) {
        this.destinationRepository = destinationRepository;
        this.attractionRepository = attractionRepository;
    }

    public AttractionEntity createAttraction(Long destinationId, AttractionDto attractionDto) {
        // Correctly use the injected repository instance
        DestinationEntity destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        AttractionEntity attractionEntity = convertToEntity(attractionDto, destination);
        attractionEntity.setDestination(destination);

        return attractionRepository.save(attractionEntity);
    }

    // Fetch all attractions for a destination
    public List<AttractionDto> getAttractionsByDestination(DestinationEntity destination) {
        return attractionRepository.findByDestination(destination).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Fetch an attraction by name and destination
    public Optional<AttractionDto> getAttractionByNameAndDestination(String name, DestinationEntity destination) {
        return attractionRepository.findByNameAndDestination(name, destination)
                .map(this::convertToDto);
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

    private AttractionEntity convertToEntity(AttractionDto dto, DestinationEntity destination) {
        return new AttractionEntity(
                dto.name(),
                dto.description(),
                dto.openingHours(),
                dto.ticketPrice(),
                dto.imageUrl(),
                destination
        );
    }

}

