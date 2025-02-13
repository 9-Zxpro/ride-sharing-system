package me.jibajo.ride_managemant_service.dto;

public record RideOverviewResponse(
        DistanceMatrixResponse.Duration duration,
        DistanceMatrixResponse.Distance distance,
        double fare
) {}
