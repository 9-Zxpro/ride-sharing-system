package me.jibajo.ride_managemant_service.dto;

import me.jibajo.ride_managemant_service.enums.RideStatus;

public record RideStatusUpdateDTO(
//        @NotNull(message = "Status is required")
        RideStatus newStatus,

//        @Size(max = 255, message = "Reason must be less than 255 characters")
        String cancellationReason,

        String driverNotes
) {
    // Validation for cancellation reason when status is CANCELLED
    public RideStatusUpdateDTO {
        if (newStatus == RideStatus.CANCELLED && (cancellationReason == null || cancellationReason.isBlank())) {
            throw new IllegalArgumentException("Cancellation reason is required when cancelling a ride");
        }
    }
}