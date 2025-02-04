package me.jibajo.ride_managemant_service.services;

public interface PricingStrategy {
    double calculateFare(double distance);
}
