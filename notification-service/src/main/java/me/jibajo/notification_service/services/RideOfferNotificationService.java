package me.jibajo.notification_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.notification_service.dto.RideOffer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideOfferNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Listens for ride offer messages from RabbitMQ.
     * When a ride offer is received, it sends it to the WebSocket topic for the target captain.
     */
    @RabbitListener(queues = "${ride-offer.queue")
    public void handleRideOffer(RideOffer offer) {
        Long captainId = offer.captainId();
        // Send the ride offer message to the topic that the captain is subscribed to.
        // For example, the captain's mobile app subscribes to: /topic/captain/{captainId}
        String destination = "/topic/captain/" + captainId;
        messagingTemplate.convertAndSend(destination, offer);
        System.out.println("Published ride offer to captain " + captainId + " at destination: " + destination);
    }
}
