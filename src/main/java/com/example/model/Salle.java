package com.example.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "rooms")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salleId;

    @NotBlank(message = "Room name cannot be empty")
    @Size(min = 2, max = 100)
    @Column(name = "room_name", nullable = false)
    private String nomSalle;

    @NotNull(message = "Capacity must be specified")
    @Min(value = 1, message = "Minimum capacity is 1")
    @Max(value = 1200, message = "Capacity limit exceeded")
    @Column(nullable = false)
    private Integer capacity;

    @Size(max = 400, message = "Description is too long")
    @Column(length = 400)
    private String details;

    @NotNull(message = "Availability status is required")
    @Column(nullable = false)
    private Boolean available = true;

    @Min(value = 0, message = "Floor number cannot be negative")
    private Integer floorNumber;

    // Empty constructor required by JPA specification
    public Salle() {
    }

    // Constructor with main attributes
    public Salle(String nomSalle, Integer capacity) {
        this.nomSalle = nomSalle;
        this.capacity = capacity;
    }

    // Accessor methods
    public Long getSalleId() {
        return salleId;
    }

    public void setSalleId(Long salleId) {
        this.salleId = salleId;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public String toString() {
        return "Room: " + nomSalle + " | Capacity: " + capacity + " | Available: " + available;
    }
}
