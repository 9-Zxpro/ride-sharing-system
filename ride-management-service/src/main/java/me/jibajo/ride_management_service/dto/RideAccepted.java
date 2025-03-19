package me.jibajo.ride_management_service.dto;

import java.io.Serializable;

public record RideAccepted(
        String otp,
        String phone,
        String vehicleNumber
) implements Serializable { }