package me.jibajo.ride_managemant_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_managemant_service.config.RabbitConfig;
import me.jibajo.ride_managemant_service.dto.GeoPoint;
import me.jibajo.ride_managemant_service.dto.RideRequestDto;
import me.jibajo.ride_managemant_service.dto.RideResponseDto;
import me.jibajo.ride_managemant_service.dto.RideStatusUpdateDTO;
import me.jibajo.ride_managemant_service.entities.Location;
import me.jibajo.ride_managemant_service.entities.Ride;
import me.jibajo.ride_managemant_service.enums.RideStatus;
import me.jibajo.ride_managemant_service.events.RideRequestEvent;
import me.jibajo.ride_managemant_service.exceptions.DistanceCalculationException;
import me.jibajo.ride_managemant_service.exceptions.InvalidStatusTransitionException;
import me.jibajo.ride_managemant_service.exceptions.RideNotFoundException;
import me.jibajo.ride_managemant_service.exceptions.RideRequestException;
import me.jibajo.ride_managemant_service.repositories.RideRepository;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final DistanceCalculatorService distanceCalculator;
    private final RabbitTemplate rabbitTemplate;
    private final PricingStrategy pricingStrategy;
    private final ModelMapper modelMapper;

    @Transactional
    public RideResponseDto createRide(RideRequestDto rideRequest) {
        validateRideRequest(rideRequest);

        try {
            double distance = distanceCalculator.calculateDistance(
                    new Location(rideRequest.getPickupLocation().getLatitude(), rideRequest.getPickupLocation().getLatitude()),
                    new Location(rideRequest.getDropoffLocation().getLatitude(), rideRequest.getDropoffLocation().getLongitude())
            );

            double fare = pricingStrategy.calculateFare(distance);

            Ride ride = new Ride();
            ride.setDistance(distance);
            ride.setFare(fare);
            ride.setStatus(RideStatus.REQUESTED);
            ride.setCreatedAt(Instant.now());

            Ride savedRide = rideRepository.save(ride);

            // Publish ride request event
            publishRideRequestEvent(savedRide);

            return convertToDto(savedRide);

        } catch (DistanceCalculationException e) {
            throw new RideRequestException("Could not calculate route distance "+ e.getMessage());
        }
    }

    @Transactional
    public RideResponseDto updateRideStatus(Long rideId, RideStatusUpdateDTO statusUpdate) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + rideId));

        validateStatusTransition(ride.getStatus(), statusUpdate.newStatus());

        ride.setStatus(statusUpdate.newStatus());
        ride.setUpdatedAt(Instant.now());

        if (statusUpdate.newStatus() == RideStatus.COMPLETED) {
            ride.setRideEndTime(Instant.now());
        }

        Ride updatedRide = rideRepository.save(ride);
        return convertToDto(updatedRide);
    }

    public RideResponseDto getRideDetails(Long rideId) {
        return rideRepository.findById(rideId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + rideId));
    }
    private void validateRideRequest(RideRequestDto rideRequest) {
        if (rideRequest.getRiderId() == null) {
            throw new RideRequestException("Invalid rider ID");
        }

        if (isValidLocation(rideRequest.getPickupLocation().getLatitude(),
                rideRequest.getPickupLocation().getLongitude())
                ||
                isValidLocation(rideRequest.getDropoffLocation().getLatitude(),
                        rideRequest.getDropoffLocation().getLongitude())) {
            throw new RideRequestException("Invalid location coordinates");
        }
    }

    private boolean isValidLocation(Double lat, Double lng) {
        return lat == null || lng == null ||
                lat < -90 || lat > 90 ||
                lng < -180 || lng > 180;
    }

    private void validateStatusTransition(RideStatus currentStatus, RideStatus newStatus) {
        if (!newStatus.isAllowedTransitionFrom(currentStatus)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }
    private void publishRideRequestEvent(Ride ride) {
        try {
            RideRequestEvent event = new RideRequestEvent(
                    ride.getId(),
                    ride.getRiderId(),
                    new GeoPoint(ride.getPickupLocation().getLatitude(), ride.getPickupLocation().getLongitude()),
                    new GeoPoint(ride.getDropoffLocation().getLatitude(), ride.getDropoffLocation().getLongitude()),
                    ride.getCreatedAt(),
                    ride.getFare()
            );

            rabbitTemplate.convertAndSend(
                    RabbitConfig.RIDE_EXCHANGE,
                    "ride-requests",
                    event);

        } catch (Exception e) {
            // TODO: implementing a retry mechanism or dead-letter queue
        }
    }

    public RideResponseDto getRideById(Long rideId) {
        Optional<Ride> ride = rideRepository.findById(rideId);
        return ride.map(this::convertToDto).orElse(null);
    }


    public RideResponseDto convertToDto(Ride ride) {
        return modelMapper.map(ride, RideResponseDto.class);
    }

}