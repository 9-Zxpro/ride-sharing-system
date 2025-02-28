package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.config.RideMatchingProperties;
import me.jibajo.ride_matching_service.dto.FoundCaptain;
import me.jibajo.ride_matching_service.dto.BookRideRequestEvent;
import me.jibajo.ride_matching_service.dto.SuitableCaptain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideMatchingService {
    private final CaptainLocationService captainLocationService;
    private final RabbitTemplate rabbitTemplate;
    private final RideMatchingProperties properties;

    private static final Logger logger = LoggerFactory.getLogger(RideMatchingService.class);

    @Value("${found-captain.exchange}")
    private String rideMatchingCaptainExchange;

    @Value("${found-captain.queue}")
    private String rideMatchingCaptainQueue;

    @Value("${found-captain.routing-key}")
    private String rideMatchingCaptainRoutingKey;

    @RabbitListener(queues = "${ride-booking-request.queue}")
    public void handleRideRequest(BookRideRequestEvent event) {
            logger.info("Ride booking request consumed");
            List<FoundCaptain> nearbyDrivers = captainLocationService.findNearbyCaptains(
                    event.pickupGeoPoint().longitude(),
                    event.pickupGeoPoint().latitude(),
                    properties.getSearchRadius()
            );

            if (nearbyDrivers.isEmpty()) {
                // TODO: Trigger retry logic
//                List<FoundCaptain> bestCaptains = selectBestDriver(nearbyDrivers);
//                publishRideOfferMessage(event, bestCaptains);
                return;
            }

            List<FoundCaptain> candidateCaptains = selectBestDriver(nearbyDrivers);
            List<SuitableCaptain> suitableCaptains = createSuitableCaptain(candidateCaptains, event);

            // Publish candidate captains
            publishCandidateCaptains(suitableCaptains);
            logger.info("Suitable captains published");
//            updateDriverStatus(bestCaptains);

    }

    private List<SuitableCaptain> createSuitableCaptain(List<FoundCaptain> foundCaptains, BookRideRequestEvent event) {
        List<SuitableCaptain> suitableCaptains = new ArrayList<>();
        for (FoundCaptain fc : foundCaptains) {
            SuitableCaptain suitableCaptain = new SuitableCaptain(
                    fc.captainId(),
                    event.rideId(),
                    fc.lng(),
                    fc.lat()
            );
            suitableCaptains.add(suitableCaptain);
        }
        return suitableCaptains;
    }

    private void publishCandidateCaptains(List<SuitableCaptain> suitableCaptains) {
        rabbitTemplate.convertAndSend(
                rideMatchingCaptainExchange,
                rideMatchingCaptainRoutingKey,
                suitableCaptains
        );
    }

    private List<FoundCaptain> selectBestDriver(
            List<FoundCaptain> captains
    ) {
        // Simple selection: first 3 captain in list
        // TODO: implement scoring system based on:
        //  - Distance
        //  - Captain rating
        //  - Vehicle type match
        //  - Captain preference
        return captains.subList(0, 3);
    }

//    private double calculateFinalFare(RideRequestEvent event) {
//        // Implement surge pricing logic here
//        return event.estimatedFare();
//    }

//    private void updateDriverStatus(FoundCaptain captain) {
//        // Update Redis/database to mark driver as busy
////        driverLocationService.updateDriverStatus(driver.driverId(), DriverStatus.BUSY);
//    }
}
