package me.jibajo.ride_matching_service.dto;

import java.io.Serializable;

public record SuitableCaptain(
        Long captainId,
        Long rideId,
        double lat,
        double lng
) implements Serializable { }
