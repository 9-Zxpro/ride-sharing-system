package me.jibajo.ride_management_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_management_service.dto.DistanceMatrixResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FareCalculationService {

    public double calculateFare(DistanceMatrixResponse.Distance distance, DistanceMatrixResponse.Duration duration) {

        double distanceKm = distance.value() / 1000.0;
        double durationMinutes = duration.value() / 60.0;

//        double baseFare = calculateBaseFare(distanceKm, type);

//          TODO:Calculate per-minute fare
//        double perMinuteFare = calculatePerMinuteFare(type);
//        double timeBasedFare = durationMinutes * perMinuteFare;

        return distanceKm * 2.5;
    }
}
