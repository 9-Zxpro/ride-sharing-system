package me.jibajo.ride_managemant_service.dto;

import java.io.Serializable;

public record RideOffer(
        Long rideId,
        Long captainId,
        String pickupAddress,
        String dropoffAddress,
        Double pickupDistance,
        Double dropDistance,
        Double fare
) implements Serializable { }