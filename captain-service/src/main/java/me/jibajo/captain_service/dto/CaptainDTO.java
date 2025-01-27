package me.jibajo.captain_service.dto;

import lombok.Data;

@Data
public class CaptainDTO {
    private Long captainId;
    private String email;
    private String phone;
    private String drivingLicenseUrl;
    private String vehicleModel;
    private String vehicleRegistrationNumber;
    private String vehicleRegistrationUrl;
}
