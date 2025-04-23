package com.test.travelplanner.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


import java.util.Objects;

@Entity
@Table(name = "trips")
public class TripEntity {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;
    @Column(name = "user_id") //foreign key
    private Long userId;
    @Column(name = "destination_id") //foreign key
    private Long cityId;
    private String title;
    private Integer days;
    private String startDate;
    private String status;
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "trips_user_id_fkey"), insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "destination_id", foreignKey = @ForeignKey(name = "trips_destination_id_fkey"), insertable = false, updatable = false)
    private DestinationEntity destination;


    public TripEntity() {
    }

    public TripEntity(Long id, Long userId, Long cityId, String title, Integer days, String startDate, String status, String notes, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.userId = userId;
        this.cityId = cityId;
        this.title = title;
        this.days = days;
        this.startDate = startDate;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDays() {
        return days;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public UserEntity getUser() {
        return user;
    }

    public DestinationEntity getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEntity that = (TripEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(cityId, that.cityId) && Objects.equals(title, that.title) && Objects.equals(days, that.days) && Objects.equals(startDate, that.startDate) && Objects.equals(status, that.status) && Objects.equals(notes, that.notes) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(user, that.user) && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, cityId, title, days, startDate, status, notes, createdAt, updatedAt, user, destination);
    }

    @Override
    public String toString() {
        return "TripEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", cityId=" + cityId +
                ", title='" + title + '\'' +
                ", days=" + days +
                ", startDate='" + startDate + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", user=" + user +
                ", destination=" + destination +
                '}';
    }
}


