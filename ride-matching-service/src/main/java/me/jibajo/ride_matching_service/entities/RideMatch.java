package me.jibajo.ride_matching_service.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RideMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long matchId;

    private Long rideId;

    private Long captainId;

    private Instant matchedAt;

    @Enumerated(EnumType.STRING)
    private MatchStatus status; // e.g., PENDING, CONFIRMED, REJECTED

    // Getters and Setters
}
