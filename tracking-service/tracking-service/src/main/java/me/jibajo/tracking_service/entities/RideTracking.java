package me.jibajo.tracking_service.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RideTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long rideId;

    private Long captainId;

    private Instant timestamp;

    @Embedded
    private Location currentLocation;

    // Getters and Setters
}
