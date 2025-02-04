package me.jibajo.admin_service.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserManagementLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private String action;

    private Long adminId;

    private Long targetUserId;

    private LocalDateTime timestamp;
}
