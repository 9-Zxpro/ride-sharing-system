package me.jibajo.rider_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "riders")
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riderId;

    @Column(unique = true)
    private String email;
    @Column(unique = true, nullable = false)
    private String phone;

    private Boolean isActive = true;
    private Boolean emailVerified = false;
    private Boolean phoneVerified = false;

    @CreatedDate
    private LocalDateTime createdAt;

}
