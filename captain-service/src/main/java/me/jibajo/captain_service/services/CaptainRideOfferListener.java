package me.jibajo.captain_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.RideOffer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CaptainRideOfferListener {
    private final RestTemplate restTemplate;

    @RabbitListener(queues = "#{@captainQueues}")
    public void handleRideOffer(RideOffer offer) {
        // Show notification in captain's app
        System.out.println("New ride offer: " + offer);

        // Simulate captain response
        if (Math.random() > 0.5) {
            acceptRide(offer.rideId());
        }
    }

    private void acceptRide(String rideId) {
        // Call RideService to confirm acceptance
        restTemplate.put(
                "http://ride-service/rides/{rideId}/accept",
                null,
                rideId
        );
    }
}
