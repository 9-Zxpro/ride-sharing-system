package me.jibajo.ride_matching_service.dto;

import me.jibajo.ride_matching_service.enums.DriverStatus;

public record DriverLocation(
        Long driverId,
        double latitude,
        double longitude,
        DriverStatus status
) {}

