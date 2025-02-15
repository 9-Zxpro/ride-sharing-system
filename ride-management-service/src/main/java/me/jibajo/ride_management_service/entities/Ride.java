package me.jibajo.ride_management_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.jibajo.ride_management_service.enums.RideStatus;

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
    private String origin;
    private String destination;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude"))
    })
    private Location pickup;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "dropoff_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dropoff_longitude"))
    })
    private Location dropOff;

    private Instant rideStartTime;
    private Instant rideEndTime;

    private Double distance;
    private Double fare;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Column(nullable = false)
    private Instant createdAt;
    private Instant updatedAt;
}
