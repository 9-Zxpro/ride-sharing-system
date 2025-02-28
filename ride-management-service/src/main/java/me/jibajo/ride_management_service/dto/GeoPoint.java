package me.jibajo.ride_management_service.dto;

import java.io.Serializable;

public record GeoPoint(
        //        @DecimalMin("-180.0") @DecimalMax("180.0")
        double longitude,
//        @DecimalMin("-90.0") @DecimalMax("90.0")
        double latitude
) implements Serializable {}
