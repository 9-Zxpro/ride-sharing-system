package me.jibajo.ride_managemant_service.dto;

import java.io.Serializable;

public record SuitableCaptain(
        Long captainId,
        Long rideId,
        double lat,
        double lng
) implements Serializable { }
