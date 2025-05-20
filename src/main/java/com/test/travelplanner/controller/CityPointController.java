package com.test.travelplanner.controller;

import com.test.travelplanner.model.entity.CityPoint;
import com.test.travelplanner.service.impl.CityPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.*;  

import java.util.List;  

@RestController  
@RequestMapping("/api/city-points")  
@RequiredArgsConstructor
@Tag(name = "CityPoint相关操作", description = "CityPoint相关接口")
public class CityPointController {  

    private final CityPointService cityPointService;

    @PostMapping
    @Operation(summary = "创建新的CityPoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CityPoint> create(@RequestBody CityPoint cityPoint) {
        return ResponseEntity.ok(cityPointService.create(cityPoint));  
    }  

    @GetMapping("/{id}")
    @Operation(summary = "通过ID获取特定的CityPoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CityPoint> findById(@PathVariable Long id) {  
        return cityPointService.findById(id)  
            .map(ResponseEntity::ok)  
            .orElse(ResponseEntity.notFound().build());  
    }  

    @GetMapping("/trip/{tripId}")
    @Operation(summary = "通过tripId获取所有相关的CityPoint ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CityPoint>> findByTripId(@PathVariable Long tripId) {  
        return ResponseEntity.ok(cityPointService.findByTripId(tripId));  
    }  

    @GetMapping("/user/{userId}")
    @Operation(summary = "通过userId获取所有相关的CityPoint ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CityPoint>> findByUserId(@PathVariable Long userId) {  
        return ResponseEntity.ok(cityPointService.findByUserId(userId));  
    }  

    @PutMapping("/{id}")
    @Operation(summary = "更新特定ID的CityPoint ", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CityPoint> update(  
            @PathVariable Long id,   
            @RequestBody CityPoint cityPoint) {  
        return ResponseEntity.ok(cityPointService.update(id, cityPoint));  
    }  

    @DeleteMapping("/{id}")
    @Operation(summary = "删除特定ID的CityPoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> delete(@PathVariable Long id) {  
        cityPointService.delete(id);  
        return ResponseEntity.noContent().build();  
    }  
}