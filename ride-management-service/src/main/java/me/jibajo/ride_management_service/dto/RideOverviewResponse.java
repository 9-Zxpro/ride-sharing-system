package me.jibajo.ride_management_service.dto;

public record RideOverviewResponse(
        DistanceMatrixResponse.Duration duration,
        DistanceMatrixResponse.Distance distance,
        double fare
) {}
