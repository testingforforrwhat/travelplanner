// Trip.java - 主实体类
package com.test.travelplanner.model.entity.trip;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "banner_image")
    private String bannerImage;
    
    private Integer duration;
    
    private String location;
    
    private BigDecimal price;
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripOverview> overview = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripHighlight> highlights = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    private List<TripItinerary> itinerary = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripPriceInclude> priceIncludes = new ArrayList<>();
    
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripPriceExclude> priceExcludes = new ArrayList<>();
    
    // 辅助方法，简化添加关联实体
    public void addOverview(TripOverview overview) {
        this.overview.add(overview);
        overview.setTrip(this);
    }
    
    public void addHighlight(TripHighlight highlight) {
        this.highlights.add(highlight);
        highlight.setTrip(this);
    }
    
    public void addItinerary(TripItinerary itinerary) {
        this.itinerary.add(itinerary);
        itinerary.setTrip(this);
    }
    
    public void addPriceInclude(TripPriceInclude priceInclude) {
        this.priceIncludes.add(priceInclude);
        priceInclude.setTrip(this);
    }
    
    public void addPriceExclude(TripPriceExclude priceExclude) {
        this.priceExcludes.add(priceExclude);
        priceExclude.setTrip(this);
    }
}