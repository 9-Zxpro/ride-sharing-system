package me.jibajo.ride_managemant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jibajo.ride_managemant_service.entities.Location;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private Long riderId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private Instant rideStartTime;
    private Instant rideEndTime;
}

