// TripItinerary.java
package com.test.travelplanner.model.entity.trip;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip_itineraries")
@Data
public class TripItinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(name = "day_number")
    private Integer dayNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripActivity> activities = new ArrayList<>();
    
    // 辅助方法
    public void addActivity(TripActivity activity) {
        activities.add(activity);
        activity.setItinerary(this);
    }
}