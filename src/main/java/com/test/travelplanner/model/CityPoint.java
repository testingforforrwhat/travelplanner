package com.test.travelplanner.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;  


import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "city_points", indexes = {
    @Index(name = "idx_trip_id", columnList = "trip_id"),  
    @Index(name = "idx_user_id", columnList = "user_id")  
})  
public class CityPoint {  

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @Column(name = "city_name", nullable = false)
    private String cityName;  

    @Column(name = "user_id", nullable = false)  
    private Long userId;  

    @Column(name = "trip_id", nullable = false)  
    private Long tripId;  

    @Column(name = "poi_id", nullable = false)  
    private Long poiId;  

    @Column(name = "visit_order", nullable = false)  
    private Integer visitOrder;  

    @Column(name = "poi_name", nullable = false)  
    private String poiName;  

    @Column(name = "poi_description", columnDefinition = "TEXT")  
    private String poiDescription;  

    @Column(name = "city_description", columnDefinition = "TEXT")  
    private String cityDescription;  

    @CreationTimestamp  
    @Column(name = "created_at", nullable = false, updatable = false)  
    private LocalDateTime createdAt;  

    @UpdateTimestamp  
    @Column(name = "updated_at", nullable = false)  
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityPoint cityPoint = (CityPoint) o;
        return Objects.equals(id, cityPoint.id) && Objects.equals(cityName, cityPoint.cityName) && Objects.equals(userId, cityPoint.userId) && Objects.equals(tripId, cityPoint.tripId) && Objects.equals(poiId, cityPoint.poiId) && Objects.equals(visitOrder, cityPoint.visitOrder) && Objects.equals(poiName, cityPoint.poiName) && Objects.equals(poiDescription, cityPoint.poiDescription) && Objects.equals(cityDescription, cityPoint.cityDescription) && Objects.equals(createdAt, cityPoint.createdAt) && Objects.equals(updatedAt, cityPoint.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityName, userId, tripId, poiId, visitOrder, poiName, poiDescription, cityDescription, createdAt, updatedAt);
    }

}