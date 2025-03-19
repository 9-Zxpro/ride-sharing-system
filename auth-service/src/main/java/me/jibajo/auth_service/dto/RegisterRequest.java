package me.jibajo.auth_service.dto;

public record RegisterRequest(
    @NotBlank String email,
    @NotBlank String password,
    Role role
) {}