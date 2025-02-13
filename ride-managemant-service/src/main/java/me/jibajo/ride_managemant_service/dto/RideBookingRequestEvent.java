package me.jibajo.ride_managemant_service.dto;

import me.jibajo.ride_managemant_service.entities.Location;

import java.io.Serializable;
import java.util.Objects;

public record RideBookingRequestEvent(
        Long rideId,
        Long riderId,
        String pickup,
        String dropOff,
        Location pickupGeoPoint,
        Location dropoffGeoPoint,
        Double distance
) implements Serializable {

    // Compact constructor for validation
    public RideBookingRequestEvent {
        Objects.requireNonNull(rideId, "Ride ID must not be null");
        Objects.requireNonNull(pickup, "Pickup location must not be null");
        Objects.requireNonNull(dropOff, "DropOff location must not be null");
        Objects.requireNonNull(pickupGeoPoint, "PickupGeoPoint must not be null");
        Objects.requireNonNull(dropoffGeoPoint, "DropoffGeoPoint must not be null");
    }
}
