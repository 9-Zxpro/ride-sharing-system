package me.jibajo.ride_management_service.dto;

public record BookRideRequest(
        Long riderId,
        String pickup,
        String dropOff,
        GeoPoint pickupGeoPoint,
        GeoPoint dropoffGeoPoint,
        DistanceMatrixResponse.Duration duration,
        DistanceMatrixResponse.Distance distance,
        double fare
) { }
