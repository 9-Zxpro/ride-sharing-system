package me.jibajo.rider_service.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "riders")
public class Rider {
    @Id
    private Long riderId; // Shared with Authentication Service

    private String email;
    private String phone;
    private String password;

    private boolean isActive;

    private LocalDateTime createdAt;
}
