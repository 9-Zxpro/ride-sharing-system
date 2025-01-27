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
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id")
    @JsonManagedReference
    private Vehicle vehicle;

    @Column(unique = true, nullable = false)
    private String drivingLicenseUrl;

    private Boolean isAvailable = false;
    private Boolean isActive = true;
    private Boolean emailVerified = false;
    private Boolean phoneVerified = false;

    @CreatedDate
    private LocalDateTime createdAt;

}

