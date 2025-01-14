package me.jibajo.captain_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "captains")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Captain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long captainId;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
//    @Getter(value = AccessLevel.NONE)
    private String password;

    @Column(unique = true, nullable = false)
    @Embedded
    private Vehicle vehicle;

    @Column(unique = true, nullable = false)
    private String drivingLicenseUrl;

    private Boolean isAvailable;
    private Boolean isActive;
    private Boolean emailVerified = false;
    private Boolean phoneVerified = false;

    @CreatedDate
    private LocalDateTime createdAt;
}

