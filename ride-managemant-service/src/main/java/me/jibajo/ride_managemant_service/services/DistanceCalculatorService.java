package me.jibajo.ride_managemant_service.services;

import me.jibajo.ride_managemant_service.entities.Location;
import me.jibajo.ride_managemant_service.exceptions.DistanceCalculationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DistanceCalculatorService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    @Value("${google.maps.distance-matrix-url}")
    private String distanceMatrixUrl;

    @Value("${google.maps.units}")
    private String units;

    private final RestTemplate restTemplate;

    public DistanceCalculatorService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public double calculateDistance(Location start, Location end) {
        try {
            String origin = String.format("%s,%s", start.getLatitude(), start.getLongitude());
            String destination = String.format("%s,%s", end.getLatitude(), end.getLongitude());

            String url = String.format("%s?units=%s&origins=%s&destinations=%s&key=%s",
                    distanceMatrixUrl, units, origin, destination, apiKey);

            DistanceMatrixResponse response = restTemplate.getForObject(url, DistanceMatrixResponse.class);

            if (response == null || !"OK".equals(response.status())) {
                throw new DistanceCalculationException("Failed to get distance from Google Maps API");
            }

            DistanceMatrixElement element = response.rows().get(0).elements().get(0);

            if (!"OK".equals(element.status())) {
                throw new DistanceCalculationException("Distance calculation failed: " + element.status());
            }

            return element.distance().value() / 1000.0; // Convert meters to kilometers
        } catch (Exception e) {
            throw new DistanceCalculationException("Could not calculate distance "+ e.getMessage());
        }
    }

    // Response record classes
    public record DistanceMatrixResponse(String status, List<Row> rows) {}
    public record Row(List<DistanceMatrixElement> elements) {}
    public record DistanceMatrixElement(String status, Distance distance) {}
    public record Distance(String text, int value) {}
}

