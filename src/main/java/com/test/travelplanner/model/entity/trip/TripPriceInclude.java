// TripPriceInclude.java
package com.test.travelplanner.model.entity.trip;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trip_price_includes")
@Data
public class TripPriceInclude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}