package me.jibajo.captain_service.dto;

import me.jibajo.captain_service.enums.CaptainStatus;

import java.io.Serializable;

public record CaptainOnDuty (
    CaptainStatus status,
    Double lat,
    Double lng
) implements Serializable {}
