package me.jibajo.ride_managemant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jibajo.ride_managemant_service.entities.Location;
import me.jibajo.ride_managemant_service.enums.RideStatus;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDto {
    private Long riderId;
    private Long captainId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private Instant rideStartTime;
    private Instant rideEndTime;
    private Double distance;
    private Double fare;
    private RideStatus status;
    private Instant createdAt;
}
