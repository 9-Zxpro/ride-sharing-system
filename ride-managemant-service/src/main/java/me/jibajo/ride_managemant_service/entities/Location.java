package me.jibajo.ride_managemant_service.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private String latitude;
    private String longitude;
    private String address;
}
