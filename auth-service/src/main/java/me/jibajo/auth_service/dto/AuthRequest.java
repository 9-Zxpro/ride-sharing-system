package me.jibajo.auth_service.dto;

public record AuthRequest(
    @NotBlank String email,
    @NotBlank String password
) {}