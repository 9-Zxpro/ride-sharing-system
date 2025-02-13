package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.config.RideMatchingProperties;
import me.jibajo.ride_matching_service.dto.FoundCaptain;
import me.jibajo.ride_matching_service.dto.BookRideRequestEvent;
import me.jibajo.ride_matching_service.dto.SuitableCaptain;
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

    @Value("${found-captain.exchange}")
    private String rideMatchingCaptainExchange;

    @Value("${found-captain.queue}")
    private String rideMatchingCaptainQueue;

    @Value("${found-captain.routing-key}")
    private String rideMatchingCaptainRoutingKey;

    @RabbitListener(queues = "${ride-booking-request.queue}")
    public void handleRideRequest(BookRideRequestEvent event) {
        try {
            List<FoundCaptain> nearbyDrivers = captainLocationService.findNearbyCaptains(
                    event.pickupGeoPoint().latitude(),
                    event.pickupGeoPoint().longitude(),
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

            publishCandidateCaptains(suitableCaptains);
//            updateDriverStatus(bestCaptains);

        } catch (Exception e) {
            //some exception
        }
    }

    private List<SuitableCaptain> createSuitableCaptain(List<FoundCaptain> foundCaptains, BookRideRequestEvent event) {
        List<SuitableCaptain> suitableCaptains = new ArrayList<>();
        for (FoundCaptain fc : foundCaptains) {
            SuitableCaptain suitableCaptain = new SuitableCaptain(
                    fc.captainId(),
                    event.rideId(),
                    fc.lat(),
                    fc.lng()
            );
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
        // Simple selection: first 5 captain in list
        // TODO: implement scoring system based on:
        //  - Distance
        //  - Captain rating
        //  - Vehicle type match
        //  - Captain preference
        return captains.subList(0, 5);
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
