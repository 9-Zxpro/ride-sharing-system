package me.jibajo.ride_managemant_service.dto;


import java.util.List;

public record GeocodingResponse(
        List<Result> results,
        String status
) {
    public record Result(
            String formatted_address,
            List<AddressComponent> address_components,
            Geometry geometry
    ) {}

    public record AddressComponent(
            String long_name,
            String short_name,
            List<String> types
    ) {}

    public record Geometry(
            GeoPoint geoPoint
    ) {}

}

