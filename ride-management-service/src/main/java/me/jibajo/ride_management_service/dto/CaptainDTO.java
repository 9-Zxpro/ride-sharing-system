package me.jibajo.ride_management_service.dto;

import java.io.Serializable;

public record CaptainDTO(
        Long captainId,
        String email,
        String phone,
        String drivingLicenseUrl,
        String vehicleModel,
        String vehicleRegistrationNumber,
        String vehicleRegistrationUrl
) implements Serializable { }
