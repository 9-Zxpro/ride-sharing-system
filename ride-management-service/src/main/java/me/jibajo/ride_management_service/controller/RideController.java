package me.jibajo.ride_management_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_management_service.dto.*;
import me.jibajo.ride_management_service.services.RouteCalculatorService;
import me.jibajo.ride_management_service.services.LocalRouteService;
import me.jibajo.ride_management_service.services.RideManagerService;
import me.jibajo.ride_management_service.services.feignclients.CaptainServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideManagerService rideManagerService;
    private final RouteCalculatorService routeCalculatorService;
    private final CaptainServiceClient captainServiceClient;
    private final LocalRouteService localRouteService;

    public static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @PostMapping("/request")
    public ResponseEntity<RideResponseDto> requestRide(@RequestBody BookRideRequest bookRideRequest) {
        return ResponseEntity.ok(rideManagerService.createRide(bookRideRequest));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideResponseDto> getRideDetails(@PathVariable Long rideId) {
        return ResponseEntity.ok(rideManagerService.getRideById(rideId));
    }

//    @PatchMapping("/{rideId}/{status}")
//    public ResponseEntity<String> updateRideStatus(@PathVariable Long rideId, @RequestParam RideStatus status) {
//        rideService.updateRideStatus(rideId, status);
//        return ResponseEntity.ok("Ride status updated to: " + status);
//    }
//
//    @PatchMapping("/{rideId}/status")
//    public ResponseEntity<Ride> updateStatus(
//            @PathVariable String rideId,
//            @RequestParam RideStatus status
//    ) {
//        Ride ride = rideService.updateRideStatus(rideId, status);
//        return ResponseEntity.ok(ride);
//    }

    @PostMapping("/ride-places")
    public ResponseEntity<APIResponse> getRouteDetails(@RequestBody RideOverviewDto rideOverviewDto) {
        return ResponseEntity.ok(new APIResponse("success",
                routeCalculatorService.rideOverviewResponse(rideOverviewDto.pickupGeoPoint(),
                        rideOverviewDto.dropoffGeoPoint())));
    }

    @PostMapping("/placesMatrix")
    public ResponseEntity<APIResponse> getRouteDetail(@RequestBody RideOverviewDto rideOverviewDto) {
        return ResponseEntity.ok(new APIResponse("success",
                routeCalculatorService.calculateDistanceMatrix(rideOverviewDto.pickupGeoPoint(),
                        rideOverviewDto.dropoffGeoPoint())));
    }

//    @PostMapping("/local-routes")
//    public ResponseEntity<APIResponse> getLocalRouteDetails(@RequestBody RideOverviewDto rideOverview) {
//        return ResponseEntity.ok(new APIResponse("success",
//                localRouteService.getRouteDetails(rideOverview.pickupGeoPoint(),
//                        rideOverview.dropoffGeoPoint())));
//    }

    @PostMapping("/geopoints")
    public ResponseEntity<APIResponse> getGeoPoints(@RequestBody GeoPoint geoPoint) {
        return ResponseEntity.ok(new APIResponse("success",
                routeCalculatorService.reverseGeocode(geoPoint.latitude(), geoPoint.longitude())));
    }

    @PostMapping("/{rideId}/accept/{captainId}")
    public ResponseEntity<APIResponse> acceptRide(@PathVariable Long rideId, @PathVariable Long captainId,
                                  @RequestBody CaptainDTO captainDTO) {
        return ResponseEntity.ok(new APIResponse("Ride accepted by captain.",
                rideManagerService.rideAccepted(rideId, captainId, captainDTO)));
    }

    @PostMapping("{captainId}/start/{rideId}")
    public ResponseEntity<APIResponse> startRideProcess(@PathVariable Long rideId,
                                                         @PathVariable Long captainId,
                                                         @RequestBody GeoPoint geoPoint) {
        try {
            APIResponse rideUpdated = rideManagerService.updateRideStatusToStart(rideId);
            Boolean rideUpdatedData = rideUpdated != null ? (Boolean) rideUpdated.getData() : null;

            APIResponse captainResponse;
            boolean captainUpdated;
            try {
                captainResponse = captainServiceClient.updateCaptainStatus(captainId, geoPoint);
                captainUpdated = captainResponse != null && Boolean.TRUE.equals(captainResponse.getData());
            } catch (Exception e) {
                captainUpdated = false;
            }

            if (Boolean.TRUE.equals(rideUpdatedData) && captainUpdated) {
                return ResponseEntity.ok(new APIResponse("Ride started successfully.", true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new APIResponse("Failed to start ride or update captain status.", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("An error occurred while starting the ride.", false));
        }
    }

    @PostMapping("/{captainId}/ride-completed/{rideId}")
    public ResponseEntity<APIResponse> rideCompleted(@PathVariable Long rideId,
                                                         @PathVariable Long captainId) {
        try {
            APIResponse rideUpdated = rideManagerService.updateRideStatusToComplete(rideId);
            Boolean rideUpdatedData = rideUpdated != null ? (Boolean) rideUpdated.getData() : null;

            return ResponseEntity.ok(new APIResponse("Ride completed successfully.", true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("An error occurred while completing the ride.", false));
        }
    }


}
