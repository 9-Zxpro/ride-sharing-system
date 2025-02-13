package me.jibajo.ride_managemant_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<String> handleRideNotFoundException(RideNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRideStatusException.class)
    public ResponseEntity<String> handleInvalidRideStatusException(InvalidRideStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RideRequestException.class)
    public ResponseEntity<String> handleRideRequestException(RideRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(DistanceCalculationException.class)
    public ResponseEntity<String> handleDistanceCalculationException(DistanceCalculationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<String> handleInvalidStatusTransitionException(InvalidStatusTransitionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(RouteCalculationException.class)
    public ResponseEntity<String> handleRouteCalculationException(RouteCalculationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(CoordinatesCalculationException.class)
    public ResponseEntity<String> handleCoordinatesCalculationException(CoordinatesCalculationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("An unexpected error occurred: " + ex.getMessage());
    }
}

