package me.jibajo.captain_service.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Captain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long captainId;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
//    @Getter(value = AccessLevel.NONE)
    private String password;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    @JsonManagedReference
    private Vehicle vehicle;

    @Column(unique = true, nullable = false)
    private String drivingLicenseUrl;

    private Boolean isAvailable;
    private Boolean isActive;
    private Boolean emailVerified = false;
    private Boolean phoneVerified = false;

    @CreatedDate
    private LocalDateTime createdAt;

    public Captain(String email, String phone, String password, Vehicle vehicle,
                   String drivingLicenseUrl, Boolean isAvailable, Boolean isActive,
                   Boolean emailVerified, Boolean phoneVerified, LocalDateTime createdAt) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.vehicle = vehicle;
        this.drivingLicenseUrl = drivingLicenseUrl;
        this.isAvailable = isAvailable;
        this.isActive = isActive;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.createdAt = createdAt;
    }


}

