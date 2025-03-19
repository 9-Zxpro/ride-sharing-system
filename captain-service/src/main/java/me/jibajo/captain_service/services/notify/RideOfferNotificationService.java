package me.jibajo.captain_service.services.notify;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.RideOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideOfferNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RideOfferNotificationService.class);

    /**
     * Listens for ride offer messages from RabbitMQ.
     * When a ride offer is received, it sends it to the WebSocket topic for the target captain.
     */
    @RabbitListener(queues = "${ride-offer.queue}")
    public void handleRideOffer(RideOffer offer) {
        logger.info("ride-offer.queue consumed");

        Long captainId = offer.captainId();
        // Send the ride offer message to the topic that the captain is subscribed to.
        // For example, the captain's mobile app subscribes to: /topic/captain/{captainId}
        String destination = "/topic/captain/" + captainId;
        messagingTemplate.convertAndSend(destination, offer);
        logger.info("Published ride offer to captain {} at destination: {}", captainId, destination);
    }
}

/*
{"rideId":24,"captainId":2,"pickupAddress":"Sector 56, Noida, Uttar Pradesh","dropoffAddress":"Connaught Place, New Delhi","pickupDistance":25495.0,"dropDistance":19875.0,"fare":203.75}
 */
