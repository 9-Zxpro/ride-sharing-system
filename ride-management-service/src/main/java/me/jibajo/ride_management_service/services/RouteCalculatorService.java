package me.jibajo.ride_management_service.services;

import me.jibajo.ride_management_service.dto.DistanceMatrixResponse;
import me.jibajo.ride_management_service.dto.GeoPoint;
import me.jibajo.ride_management_service.dto.GeocodingResponse;
import me.jibajo.ride_management_service.dto.RideOverviewResponse;
import me.jibajo.ride_management_service.exceptions.CoordinatesCalculationException;
import me.jibajo.ride_management_service.exceptions.DistanceCalculationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteCalculatorService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    @Value("${google.maps.endpoints.distance-matrix}")
    private String distanceMatrixUrl;

    @Value("${google.maps.endpoints.geocoding}")
    private String geocodeUrl;

    private final RestTemplate restTemplate;
    private final FareCalculationService fareCalculationService;

    @Autowired
    public RouteCalculatorService(RestTemplate restTemplate, FareCalculationService fareCalculationService) {
        this.restTemplate = restTemplate;
        this.fareCalculationService = fareCalculationService;
    }

    public DistanceMatrixResponse calculateDistanceMatrix(GeoPoint origin, GeoPoint destination) {
        try {
            String origins = origin.latitude() + "," + origin.longitude();
            String destinations = destination.latitude() + "," + destination.longitude();
            String url = String.format("%s?origins=%s&destinations=%s&key=%s",
                    this.distanceMatrixUrl, origins, destinations, this.apiKey);

            return restTemplate.getForObject(url, DistanceMatrixResponse.class);

        } catch (Exception e) {
            throw new DistanceCalculationException("Could not calculate distance " + e.getMessage());
        }
    }

    public RideOverviewResponse rideOverviewResponse(GeoPoint origin, GeoPoint destination) {
        DistanceMatrixResponse response = calculateDistanceMatrix(origin, destination);
        if (response == null || !"OK".equals(response.status())) {
            throw new DistanceCalculationException("Failed to get distance from Google Maps API");
        }
        DistanceMatrixResponse.Distance distance = response.rows().getFirst().elements().getFirst().distance();
        DistanceMatrixResponse.Duration duration = response.rows().getFirst().elements().getFirst().duration();

        double fare = fareCalculationService.calculateFare(distance, duration);

        return new RideOverviewResponse(duration, distance, fare);
    }

    public GeocodingResponse reverseGeocode(double latitude, double longitude) {
        try {
            String geocodeUrl = String.format("%s?latlng=%.6f,%.6f&key=%s", this.geocodeUrl, latitude, longitude, this.apiKey);
            GeocodingResponse geocodingResponse = restTemplate.getForObject(geocodeUrl, GeocodingResponse.class);

            if (geocodingResponse!=null && geocodingResponse.status().equals("OK")) {
                throw new CoordinatesCalculationException("Geocoding failed. Status Code: " + geocodingResponse.status());
            }
            return geocodingResponse;
        } catch (Exception e) {
            throw new CoordinatesCalculationException("Failed to reverse geocode: " + e);
        }
    }


//        private final WebClient webClient;
//
//        public ReverseGeocodingService(WebClient.Builder webClientBuilder) {
//            this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com/maps/api/geocode").build();
//        }
//
//        public Mono<String> reverseGeocode(double latitude, double longitude, String apiKey) {
//            return webClient.get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("/json")
//                            .queryParam("latlng", latitude + "," + longitude)
//                            .queryParam("key", apiKey)
//                            .build())
//                    .retrieve()
//                    .bodyToMono(GeocodingResponse.class)
//                    .flatMap(response -> {
//                        if ("OK".equals(response.getStatus()) && !response.getResults().isEmpty()) {
//                            return Mono.just(response.getResults().get(0).getFormatted_address());
//                        } else {
//                            return Mono.error(new IOException("Reverse geocoding failed: " + (response != null ? response.getStatus() : "Unknown error")));
//                        }
//                    });
//        }

}

