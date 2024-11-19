package com.test.travelplanner.destination;

import com.test.travelplanner.destination.model.AttractionDto;
import com.test.travelplanner.destination.model.AttractionEntity;
import com.test.travelplanner.destination.model.DestinationEntity;
import com.test.travelplanner.repository.AttractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public AttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
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
                entity.getDestination().getId()
        );
    }
}

