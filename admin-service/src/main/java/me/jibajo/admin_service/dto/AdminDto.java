package me.jibajo.admin_service.dto;

import lombok.Data;
import me.jibajo.admin_service.enums.Role;

@Data
public class AdminDto {
    private String name;
    private String email;
    private String password;
    private Role role;
}
