package me.jibajo.ride_managemant_service.exceptions;

public class InvalidRideStatusException extends RuntimeException {
    public InvalidRideStatusException(String message) {
        super(message);
    }
}

