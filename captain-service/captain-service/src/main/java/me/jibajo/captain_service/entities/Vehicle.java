package me.jibajo.captain_service.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Vehicle {
    private String type = "Bike";
    private String model;
    private String vehicleRegistrationUrl;
}
