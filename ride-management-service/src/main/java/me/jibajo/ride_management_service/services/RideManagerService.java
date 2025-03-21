package me.jibajo.ride_management_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_management_service.dto.*;
import me.jibajo.ride_management_service.entities.Location;
import me.jibajo.ride_management_service.entities.Ride;
import me.jibajo.ride_management_service.enums.RideStatus;
import me.jibajo.ride_management_service.dto.RideBookingRequestEvent;
import me.jibajo.ride_management_service.exceptions.DistanceCalculationException;
import me.jibajo.ride_management_service.exceptions.RideNotFoundException;
import me.jibajo.ride_management_service.exceptions.RideRequestException;
import me.jibajo.ride_management_service.repositories.RideRepository;
import me.jibajo.ride_management_service.services.feignclients.CaptainServiceClient;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RideManagerService {

    private final RideRepository rideRepository;
    private final RouteCalculatorService routeCalculatorService;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final DynamicPricingStrategy pricingStrategy;
    private final OtpService otpService;
    private final CaptainServiceClient captainServiceClient;
    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(RideManagerService.class);

    @Value("${ride-booking-request.exchange}")
    private String rideBookingRequestExchange;

    @Value("${ride-booking-request.routing-key}")
    private String rideBookingRequestRoutingKey;

    @Value("${ride-cache-prefix}")
    private String rideCachePrefix;

    @Transactional
    public RideResponseDto createRide(BookRideRequest rideRequest) {
        validateRideRequest(rideRequest);

        try {
            RideOverviewResponse rideOverviewResponse = routeCalculatorService
                    .rideOverviewResponse(rideRequest.pickupGeoPoint(), rideRequest.dropoffGeoPoint());
            double fare = pricingStrategy.calculateFare(rideOverviewResponse.distance().value());

            Ride ride = getRide(rideRequest, rideOverviewResponse, fare);

            Ride savedRide = rideRepository.save(ride);

            // Save ride cache
            String key = rideCachePrefix + savedRide.getId();
            redisTemplate.opsForValue().set(key, savedRide);

            redisTemplate.expire(key, 5, TimeUnit.MINUTES);

            // Publish ride request event
            logger.info("ride booking request queue published.");
            publishRideBookingRequestEvent(savedRide);

            return convertToDto(savedRide);

        } catch (DistanceCalculationException e) {
            throw new RideRequestException("Could not create ride "+ e.getMessage());
        }
    }

    private static @NotNull Ride getRide(BookRideRequest rideRequest, RideOverviewResponse rideOverviewResponse, double fare) {
        Location pickupLoc = new Location(
                rideRequest.pickupGeoPoint().latitude(),
                rideRequest.pickupGeoPoint().longitude()
        );
        Location dropOffLoc = new Location(
                rideRequest.dropoffGeoPoint().latitude(),
                rideRequest.dropoffGeoPoint().longitude()
        );

        Ride ride = new Ride();
        ride.setRiderId(rideRequest.riderId());
        ride.setOrigin(rideRequest.pickup());
        ride.setDestination(rideRequest.dropOff());
        ride.setPickup(pickupLoc);
        ride.setDropOff(dropOffLoc);
        ride.setDistance(rideOverviewResponse.distance().value());
        ride.setFare(fare);
        ride.setStatus(RideStatus.REQUESTED);
        return ride;
    }

    public void updateRideStatusInCache(Long rideId, String status) {
        String key = rideCachePrefix + rideId;
        redisTemplate.opsForHash().put(key, "status", status);
    }

    public void deleteRideCache(Long rideId) {
        String key = rideCachePrefix + rideId;
        redisTemplate.delete(key);
    }

    @Transactional
    public RideResponseDto updateRideStatus(Long rideId, RideStatusUpdateDTO statusUpdate) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + rideId));

//        validateStatusTransition(ride.getStatus(), statusUpdate.newStatus());

        ride.setStatus(statusUpdate.newStatus());

        if (statusUpdate.newStatus() == RideStatus.COMPLETED) {
            ride.setRideEndTime(Instant.now().toEpochMilli());
        }

        Ride updatedRide = rideRepository.save(ride);


        return convertToDto(updatedRide);
    }

    public RideResponseDto getRideDetails(Long rideId) {
        return rideRepository.findById(rideId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + rideId));
    }
    private void validateRideRequest(BookRideRequest rideRequest) {
        if (rideRequest.riderId() == null) {
            throw new RideRequestException("Invalid rider ID");
        }

//        if (isValidLocation(rideRequest.getPickupLocation().getLatitude(),
//                rideRequest.getPickupLocation().getLongitude())
//                ||
//                isValidLocation(rideRequest.getDropoffLocation().getLatitude(),
//                        rideRequest.getDropoffLocation().getLongitude())) {
//            throw new RideRequestException("Invalid location coordinates");
//        }
    }

    private boolean isValidLocation(Double lat, Double lng) {
        return lat == null || lng == null ||
                lat < -90 || lat > 90 ||
                lng < -180 || lng > 180;
    }

//    private void validateStatusTransition(RideStatus currentStatus, RideStatus newStatus) {
//        if (!newStatus.isAllowedTransitionFrom(currentStatus)) {
//            throw new InvalidStatusTransitionException(
//                    "Invalid status transition from " + currentStatus + " to " + newStatus
//            );
//        }
//    }

    private void publishRideBookingRequestEvent(Ride ride) {
        try {
            RideBookingRequestEvent event = new RideBookingRequestEvent(
                    ride.getId(),
                    ride.getRiderId(),
                    ride.getOrigin(),
                    ride.getDestination(),
                    ride.getPickup(),
                    ride.getDropOff(),
                    ride.getDistance()
            );

            rabbitTemplate.convertAndSend(
                    rideBookingRequestExchange,
                    rideBookingRequestRoutingKey,
                    event);

        } catch (Exception e) {
            // TODO: implement a retry mechanism or dead-letter queue
        }
    }

    public APIResponse rideAccepted(Long rideId, Long captainId, CaptainDTO captainDTO){
        Ride ride = (Ride) redisTemplate.opsForValue().get(rideCachePrefix + rideId);
        if (ride == null) {
            return new APIResponse("Ride not found in cache", null);
        }

        ride.setStatus(RideStatus.DRIVER_ASSIGNED);
        ride.setCaptainId(captainId);

        redisTemplate.opsForValue().set(rideCachePrefix + rideId, ride);

        RideAccepted rideAcceptedDto = rideAcceptedResponse(rideId, captainDTO);

        return new APIResponse("Ride accepted successfully", rideAcceptedDto);
    }

    public APIResponse updateRideStatusToStart(Long rideId) {
        Ride ride = (Ride) redisTemplate.opsForValue().get(rideCachePrefix + rideId);
        if (ride == null) {
            return new APIResponse("Ride not found in cache", false);
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setRideStartTime(Instant.now().toEpochMilli());
        redisTemplate.opsForValue().set(rideCachePrefix + rideId, ride);

        return new APIResponse("Ride status updated to IN_PROGRESS in cache", true);
    }

    public APIResponse updateRideStatusToComplete(Long rideId) {
        Ride ride = (Ride) redisTemplate.opsForValue().get(rideCachePrefix + rideId);
        if (ride == null) {
            return new APIResponse("Ride not found in cache", false);
        }

        //    Save ride to persistent storage
        ride.setStatus(RideStatus.COMPLETED);
        ride.setRideEndTime(Instant.now().toEpochMilli());
        redisTemplate.opsForValue().set(rideCachePrefix + rideId, ride);
        rideRepository.save(ride);

        //    update the captain
        try {
            APIResponse captainResponse = captainServiceClient.updateCaptainStatusToONDUTY(ride.getCaptainId());
            if (!Boolean.TRUE.equals(captainResponse.getData())) {
                return new APIResponse("Ride completed but failed to update captain status", false);
            }
        } catch (Exception e) {
            logger.error("Failed to update captain status for captainId {}: {}", ride.getCaptainId(), e.getMessage());
        }

        //    inform the rider
//        try {
//            notificationService.sendRideCompletionNotification(riderId, rideId);
//        } catch (Exception e) {
//            logger.error("Failed to notify riderId {} about ride completion: {}", riderId, e.getMessage());
//        }

        return new APIResponse("Ride status updated to Complete in cache and stored in database", true);
    }

    public RideResponseDto getRideById(Long rideId) {
        Optional<Ride> ride = rideRepository.findById(rideId);
        return ride.map(this::convertToDto).orElse(null);
    }


    public RideResponseDto convertToDto(Ride ride) {
        return modelMapper.map(ride, RideResponseDto.class);
    }

    private RideAccepted rideAcceptedResponse(Long rideId, CaptainDTO captainDto){
        Ride ride = (Ride) redisTemplate.opsForValue().get(rideCachePrefix + rideId);
        List<Point> position = redisTemplate.opsForGeo().position("captains:onDuty", captainDto.captainId());
        String otp = otpService.generateOtp(rideId);

        RideAccepted rideAccepted = new RideAccepted(
                otp,
                captainDto.phone(),
                captainDto.vehicleRegistrationNumber()
        );
        return rideAccepted;
    }
}