package me.jibajo.captain_service.dto;

import me.jibajo.captain_service.enums.CaptainStatus;

import java.io.Serializable;

public record CaptainStatusCache(
    CaptainStatus status,
    GeoPoint point
) implements Serializable {}
