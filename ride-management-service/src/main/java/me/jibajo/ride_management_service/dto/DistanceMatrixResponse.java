package me.jibajo.ride_management_service.dto;

import java.util.List;

public record DistanceMatrixResponse(
        List<String> destination_addresses,
        List<String> origin_addresses,
        List<Row> rows,
        String status
) {
    public record Row(List<DistanceMatrixElement> elements) {}
    public record DistanceMatrixElement(
            String status,
            Distance distance,
            Duration duration
    ) {}
    public record Duration(String text, int value) {}
    public record Distance(String text, double value) {}
}

