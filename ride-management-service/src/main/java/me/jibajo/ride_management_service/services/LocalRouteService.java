package me.jibajo.ride_management_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jibajo.ride_management_service.dto.DistanceMatrixResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class LocalRouteService {
    private final List<DistanceMatrixResponse> responses;

    public LocalRouteService() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // File file = new File("distanceMatrix.json");
        InputStream file = getClass().getClassLoader().getResourceAsStream("static/distanceMatrix.json");

        if (file != null) {
            responses = Arrays.asList(objectMapper.readValue(file, DistanceMatrixResponse[].class));
        } else {
            throw new IOException("File not found");
        }
    }

    public DistanceMatrixResponse getRouteDetails(String origin, String destination) {

        for (DistanceMatrixResponse response : responses) {
            if (response.origin_addresses().contains(origin) &&
                    response.destination_addresses().contains(destination)) {
                return response;
            }
        }
        return null;
    }
}
