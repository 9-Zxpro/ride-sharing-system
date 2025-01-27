package me.jibajo.captain_service.requests;

import lombok.Data;

@Data
public class CaptainRegRequest {
    private String email;
    private String phone;
    private String password;
    private String drivingLicenseUrl;
    private String model;
    private String registrationNumber;
    private String registrationUrl;
}
