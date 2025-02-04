package me.jibajo.ride_matching_service.dto;

import java.io.Serializable;

public record GeoPoint(
//        @DecimalMin("-90.0") @DecimalMax("90.0")
        double latitude,

//        @DecimalMin("-180.0") @DecimalMax("180.0")
        double longitude
) implements Serializable {}
