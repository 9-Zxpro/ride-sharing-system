package me.jibajo.captain_service.entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "captains")
public class Captain {
    @Id
    private Long userId;

    private String email;
    private String phone;

    private String password;

    @Embedded
    private Vehicle vehicle;
    private String drivingLicenseUrl;

    private Boolean isAvailable;

    private Boolean isActive;

    private LocalDateTime createdAt;
}

