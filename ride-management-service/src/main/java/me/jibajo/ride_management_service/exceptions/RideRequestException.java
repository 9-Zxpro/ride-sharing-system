package me.jibajo.ride_management_service.exceptions;

public class RideRequestException extends RuntimeException {
    public RideRequestException(String message) {
        super(message);
    }
}

