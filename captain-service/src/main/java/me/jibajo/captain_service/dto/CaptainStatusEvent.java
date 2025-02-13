package me.jibajo.captain_service.dto;

import me.jibajo.captain_service.enums.CaptainStatus;

import java.io.Serializable;

public record CaptainStatusEvent(
        Long captainId,
        CaptainStatus status,
        Double lat,
        Double lng
) implements Serializable {}
