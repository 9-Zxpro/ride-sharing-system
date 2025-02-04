package me.jibajo.ride_managemant_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DynamicPricingStrategy implements PricingStrategy {

//    private final PricingConfig config;

    @Override
    public double calculateFare(double distance) {
//        double baseFare = config.getBaseFare();
//        double perKmRate = config.getPerKmRate();
        double baseFare = 5.0;
        double perKmRate = 10.0;
        return baseFare + (distance * perKmRate);
    }
}
