package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.config.RabbitConfig;
import me.jibajo.ride_matching_service.config.RideMatchingProperties;
import me.jibajo.ride_matching_service.dto.DriverLocation;
import me.jibajo.ride_matching_service.dto.DriverMatchedEvent;
import me.jibajo.ride_matching_service.dto.RideRequestEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideMatchingConsumer {
    private final DriverLocationService driverLocationService;
    private final RabbitTemplate rabbitTemplate;
    private final RideMatchingProperties properties;

    @RabbitListener(queues = RabbitConfig.RIDE_REQUESTS_QUEUE)
    public void handleRideRequest(RideRequestEvent event) {
        try {
            List<DriverLocation> nearbyDrivers = driverLocationService.findNearbyDrivers(
                    event.pickupLocation().latitude(),
                    event.pickupLocation().longitude(),
                    properties.getSearchRadius()
            );

            if (nearbyDrivers.isEmpty()) {
                // Trigger retry logic or escalation
                DriverLocation bestDriver = selectBestDriver(nearbyDrivers, event);
                publishDriverMatchedEvent(event, bestDriver);
                return;
            }

            DriverLocation bestDriver = selectBestDriver(nearbyDrivers, event);
            publishDriverMatchedEvent(event, bestDriver);
            updateDriverStatus(bestDriver);

        } catch (Exception e) {
            // Implement dead-letter queue logic
        }
    }

    private DriverLocation selectBestDriver(
            List<DriverLocation> drivers,
            RideRequestEvent event
    ) {
        // Simple selection: first driver in list
        // Could implement scoring system based on:
        // - Distance
        // - Driver rating
        // - Vehicle type match
        // - Driver preference
        return drivers.get(0);
    }

    private void publishDriverMatchedEvent(RideRequestEvent rideEvent, DriverLocation driver) {
        DriverMatchedEvent event = new DriverMatchedEvent(
                rideEvent.rideId(),
                driver.driverId(),
                rideEvent.riderId(),
                calculateFinalFare(rideEvent),
                Instant.now()
        );

        rabbitTemplate.convertAndSend(RabbitConfig.RIDE_EXCHANGE, "driver-matched", event);
    }

    private double calculateFinalFare(RideRequestEvent event) {
        // Implement surge pricing logic here
        return event.estimatedFare();
    }

    private void updateDriverStatus(DriverLocation driver) {
        // Update Redis/database to mark driver as busy
//        driverLocationService.updateDriverStatus(driver.driverId(), DriverStatus.BUSY);
    }
}
