package me.jibajo.ride_matching_service.dto;

import java.time.Instant;

public record DriverMatchedEvent(
        Long rideId,
        Long driverId,
        Long riderId,
        double fare,
        Instant matchedTime
) {}
