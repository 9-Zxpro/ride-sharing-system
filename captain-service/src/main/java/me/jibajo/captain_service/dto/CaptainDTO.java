package me.jibajo.captain_service.dto;

import lombok.Data;

@Data
public class CaptainDTO {
    private Long Id;
    private String email;
    private String phone;
//    private String vehicleType = "Bike";
    private String model;
    private String vehicleRegNumber;
    private String vehicleRegistrationUrl;
    private String drivingLicenseUrl;
}
