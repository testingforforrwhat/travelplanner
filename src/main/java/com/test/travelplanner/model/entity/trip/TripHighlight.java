// TripHighlight.java
package com.test.travelplanner.model.entity.trip;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trip_highlights")
@Data
public class TripHighlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}