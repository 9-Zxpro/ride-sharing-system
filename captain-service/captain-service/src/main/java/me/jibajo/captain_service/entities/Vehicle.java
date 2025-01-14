package me.jibajo.captain_service.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Vehicle {
//    private String vehicleType = "Bike";
    private String model;
    private String vrNumber;
    private String vehicleRegistrationUrl;
}
