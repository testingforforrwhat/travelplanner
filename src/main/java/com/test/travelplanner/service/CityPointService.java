package com.test.travelplanner.service;

import com.test.travelplanner.model.CityPoint;
import com.test.travelplanner.repository.CityPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;  
import org.springframework.transaction.annotation.Transactional;  

import java.util.List;  
import java.util.Optional;  

@Service  
@RequiredArgsConstructor  
@Transactional(readOnly = true)  
public class CityPointService {  

    private final CityPointRepository cityPointRepository;

    @Transactional  
    public CityPoint create(CityPoint cityPoint) {
        if (cityPoint.getVisitOrder() == null) {  
            Integer maxOrder = cityPointRepository.findMaxVisitOrderByTripId(cityPoint.getTripId());  
            cityPoint.setVisitOrder(maxOrder == null ? 1 : maxOrder + 1);  
        }  
        return cityPointRepository.save(cityPoint);  
    }  

    public Optional<CityPoint> findById(Long id) {  
        return cityPointRepository.findById(id);  
    }  

    public List<CityPoint> findByTripId(Long tripId) {  
        return cityPointRepository.findByTripIdOrderByVisitOrder(tripId);  
    }  

    public List<CityPoint> findByUserId(Long userId) {  
        return cityPointRepository.findByUserIdOrderByCreatedAtDesc(userId);  
    }  

    @Transactional  
    public CityPoint update(Long id, CityPoint cityPoint) {  
        return cityPointRepository.findById(id)  
            .map(existing -> {  
                existing.setCityName(cityPoint.getCityName());  
                existing.setPoiName(cityPoint.getPoiName());  
                existing.setPoiDescription(cityPoint.getPoiDescription());  
                existing.setCityDescription(cityPoint.getCityDescription());  
                existing.setVisitOrder(cityPoint.getVisitOrder());  
                return cityPointRepository.save(existing);  
            })  
            .orElseThrow(() -> new RuntimeException("CityPoint not found: " + id));  
    }  

    @Transactional  
    public void delete(Long id) {  
        cityPointRepository.deleteById(id);  
    }  
}