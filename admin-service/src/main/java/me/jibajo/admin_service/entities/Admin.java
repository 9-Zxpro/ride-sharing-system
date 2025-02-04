package me.jibajo.admin_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.jibajo.admin_service.enums.Role;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
