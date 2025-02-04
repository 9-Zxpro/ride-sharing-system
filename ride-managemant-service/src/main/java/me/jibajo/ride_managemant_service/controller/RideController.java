package me.jibajo.ride_managemant_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_managemant_service.dto.RideRequestDto;
import me.jibajo.ride_managemant_service.dto.RideResponseDto;
import me.jibajo.ride_managemant_service.entities.Ride;
import me.jibajo.ride_managemant_service.enums.RideStatus;
import me.jibajo.ride_managemant_service.services.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private RideService rideService;

    @PostMapping("/request")
    public ResponseEntity<RideResponseDto> requestRide(@RequestBody RideRequestDto rideRequestDto) {
        return ResponseEntity.ok(rideService.createRide(rideRequestDto));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideResponseDto> getRideDetails(@PathVariable Long rideId) {
        return ResponseEntity.ok(rideService.getRideById(rideId));
    }

    @PatchMapping("/{rideId}/status")
    public ResponseEntity<String> updateRideStatus(@PathVariable Long rideId, @RequestParam RideStatus status) {
        rideService.updateRideStatus(rideId, status);
        return ResponseEntity.ok("Ride status updated to: " + status);
    }

    @PatchMapping("/{rideId}/status")
    public ResponseEntity<Ride> updateStatus(
            @PathVariable String rideId,
            @RequestParam RideStatus status
    ) {
        Ride ride = rideService.updateRideStatus(rideId, status);
        return ResponseEntity.ok(ride);
    }
}
