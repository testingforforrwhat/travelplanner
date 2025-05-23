// TripPriceExclude.java
package com.test.travelplanner.model.entity.trip;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trip_price_excludes")
@Data
public class TripPriceExclude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "trip_id")
    private Trip trip;
}