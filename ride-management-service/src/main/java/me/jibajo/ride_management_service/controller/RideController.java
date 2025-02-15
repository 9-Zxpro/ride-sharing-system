package me.jibajo.ride_management_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_management_service.dto.*;
import me.jibajo.ride_management_service.services.RouteCalculatorService;
import me.jibajo.ride_management_service.services.LocalRouteService;
import me.jibajo.ride_management_service.services.RideManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideManagerService rideManagerService;
    private final RouteCalculatorService routeCalculatorService;
    private final LocalRouteService localRouteService;

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
    public APIResponse acceptRide(@PathVariable Long rideId, @PathVariable Long captainId) {
        return new APIResponse("Ride accepted by captain.",
                rideManagerService.rideAccepted(rideId, captainId));
    }
}
