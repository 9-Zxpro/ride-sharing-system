package me.jibajo.ride_matching_service.dto;

import java.io.Serializable;
import java.util.Objects;


public record BookRideRequestEvent(
        Long rideId,
        Long riderId,
        String pickup,
        String dropOff,
        GeoPoint pickupGeoPoint,
        GeoPoint dropoffGeoPoint,
        Double distance
) implements Serializable {

    // Compact constructor for validation
    public BookRideRequestEvent {
        Objects.requireNonNull(rideId, "Ride ID must not be null");
        Objects.requireNonNull(pickup, "Pickup location must not be null");
        Objects.requireNonNull(dropOff, "DropOff location must not be null");
        Objects.requireNonNull(pickupGeoPoint, "PickupGeoPoint must not be null");
        Objects.requireNonNull(dropoffGeoPoint, "DropoffGeoPoint must not be null");
    }
}

