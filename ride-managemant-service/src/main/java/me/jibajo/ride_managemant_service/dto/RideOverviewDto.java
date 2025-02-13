package me.jibajo.ride_managemant_service.dto;

import java.io.Serializable;

public record RideOverviewDto (
    Long riderId,
    String pickup,
    String dropOff,
    GeoPoint pickupGeoPoint,
    GeoPoint dropoffGeoPoint
) implements Serializable {}

