package me.jibajo.captain_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vehicle {
//    private String vehicleType = "Bike";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private String registrationNumber;
    private String registrationUrl;

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Captain captain;

    public Vehicle(String model, String registrationNumber, String registrationUrl) {
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.registrationUrl = registrationUrl;
    }
 }
