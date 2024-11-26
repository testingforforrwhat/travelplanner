package com.test.travelplanner.destination.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attractions")
public class AttractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "ticket_price")
    private Double ticketPrice;

    @Column(name = "image_url")
    private String imageUrl;

    // Many-to-one relationship with DestinationEntity
    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;

    // Constructors
    public AttractionEntity() {
    }

    public AttractionEntity(String name, String description, String openingHours, Double ticketPrice, String imageUrl, DestinationEntity destination) {
        this.name = name;
        this.description = description;
        this.openingHours = openingHours;
        this.ticketPrice = ticketPrice;
        this.imageUrl = imageUrl;
        this.destination = destination;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DestinationEntity getDestination() {
        return destination;
    }

    public void setDestination(DestinationEntity destination) {
        this.destination = destination;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttractionEntity that = (AttractionEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, destination);
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "AttractionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", imageUrl='" + imageUrl + '\'' +
                ", destinationId=" + (destination != null ? destination.getId() : null) +
                '}';
    }
}

