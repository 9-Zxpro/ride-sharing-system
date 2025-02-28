package me.jibajo.ride_management_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_management_service.dto.DistanceMatrixResponse;
import me.jibajo.ride_management_service.dto.GeoPoint;
import me.jibajo.ride_management_service.dto.RideOffer;
import me.jibajo.ride_management_service.dto.SuitableCaptain;
import me.jibajo.ride_management_service.entities.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideMatchingFoundCaptain {
    private final RouteCalculatorService routeCalculatorService;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RideMatchingFoundCaptain.class);

    @Value("${ride-cache-prefix}")
    private String rideCachePrefix;

    @Value("${ride-offer.exchange}")
    private String rideOfferExchange;

    @Value("${ride-offer.routing-key}")
    private String rideOfferRoutingKey;

    @RabbitListener(queues = "${found-captain.queue}")
    public void foundRideMatchingCaptain(List<SuitableCaptain> suitableCaptains) {
        logger.info("found-captain.queue consumed");

        for(SuitableCaptain candidate: suitableCaptains) {
            Ride ride = (Ride) redisTemplate.opsForValue().get(rideCachePrefix + candidate.rideId());
            if (ride == null) {
                continue;
            }
            if (!ride.getStatus().name().equals("REQUESTED")) {
                break;
            }

            GeoPoint ridePickup = new GeoPoint(ride.getPickup().getLongitude(), ride.getPickup().getLatitude());
            GeoPoint candidateGeo = new GeoPoint(candidate.lng(), candidate.lat());

            DistanceMatrixResponse distanceResponse = routeCalculatorService.calculateDistanceMatrix(ridePickup, candidateGeo);

            double pickupDistance = distanceResponse.rows().getFirst().elements().getFirst().distance().value();

            RideOffer offer = new RideOffer(
                    ride.getId(),
                    candidate.captainId(),
                    ride.getOrigin(),
                    ride.getDestination(),
                    pickupDistance,
                    ride.getDistance(),
                    ride.getFare()
            );

            // Publish the ride offer.
            rabbitTemplate.convertAndSend(
                    rideOfferExchange,
                    rideOfferRoutingKey,
                    offer
            );
            logger.info("ride offer published");
        }
    }
}
