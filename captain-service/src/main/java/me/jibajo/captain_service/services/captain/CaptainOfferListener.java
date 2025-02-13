package me.jibajo.captain_service.services.captain;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.RideOffer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptainOfferListener {
    private final ICaptainService captainService;

//    @RabbitListener(queues = "${ride-offer.queues-prefix}")
//    public void handleRideOffer(RideOfferMessage offer, Channel channel, Message message) {
//        try {
//            if (shouldProcessMessage(offer.captainId())) {
//                processOffer(offer);
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } else {
//                // Requeue=false to remove from queue
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            }
//        } catch (IOException e) {
//            // Handle exception
//        }
//    }

//    private boolean shouldProcessMessage(Long targetCaptainId) {
//        Long currentCaptainId = captainService.getCaptainById(targetCaptainId).getCaptainId();
//        int targetGroup = (int) Long.remainderUnsigned(targetCaptainId, QUEUE_COUNT);
//        int currentGroup = (int) Long.remainderUnsigned(currentCaptainId, QUEUE_COUNT);
//
//        return targetGroup == currentGroup && targetCaptainId.equals(currentCaptainId);
//    }

    private void processOffer(RideOffer offer) {
        // Process offer logic
    }
}
