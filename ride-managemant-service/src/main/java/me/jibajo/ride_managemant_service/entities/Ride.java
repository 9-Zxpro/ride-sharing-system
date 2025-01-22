package me.jibajo.ride_managemant_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@Entity
@Table(name = "rides")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long riderId;
    private Long captainId;

    @Embedded
    private Location pickupLocation;

    @Embedded
    private Location dropoffLocation;

    private Instant rideStartTime;
    private Instant rideEndTime;

    private Double distance;
    private Double fare;

    @Enumerated(EnumType.STRING)
    private RideStatus status; // REQUESTED, ACCEPTED, ONGOING, COMPLETED, CANCELLED
}
