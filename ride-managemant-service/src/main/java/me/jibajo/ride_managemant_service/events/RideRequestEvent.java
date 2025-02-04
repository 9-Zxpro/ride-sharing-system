package me.jibajo.ride_managemant_service.events;

import me.jibajo.ride_managemant_service.dto.GeoPoint;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public record RideRequestEvent(
        Long rideId,
        Long riderId,
        GeoPoint pickupLocation,
        GeoPoint dropoffLocation,
        Instant requestTime,
        double estimatedFare
) implements Serializable {

    // Compact constructor for validation
    public RideRequestEvent {
        Objects.requireNonNull(rideId, "Ride ID must not be null");
        Objects.requireNonNull(pickupLocation, "Pickup location must not be null");
        if (estimatedFare <= 0) {
            throw new IllegalArgumentException("Fare must be positive");
        }
    }
}
