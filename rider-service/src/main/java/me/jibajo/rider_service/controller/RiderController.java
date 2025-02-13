package me.jibajo.rider_service.controller;

import me.jibajo.rider_service.dto.RiderPhoneDto;
import me.jibajo.rider_service.entities.Rider;
import me.jibajo.rider_service.dto.APIResponse;
import me.jibajo.rider_service.services.IRiderService;
import me.jibajo.rider_service.services.clients.FareAndDistanceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final IRiderService riderService;
    private final FareAndDistanceClient fareAndDistanceClient;

    public RiderController(IRiderService riderService, FareAndDistanceClient fareAndDistanceClient) {
        this.riderService = riderService;
        this.fareAndDistanceClient = fareAndDistanceClient;
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse> createRider(@RequestBody RiderPhoneDto rider) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", riderService.createRider(rider)));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{riderId}")
    public ResponseEntity<APIResponse> getRiderById(@PathVariable Long riderId) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", riderService.getRiderById(riderId)));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllRiders() {
        try {
            return ResponseEntity.ok(new APIResponse("Success", riderService.getAllRiders()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }

//    TODO: use @PatchMapping to update details
    @PutMapping("/{riderId}")
    public ResponseEntity<APIResponse> updateRider(@PathVariable Long riderId, @RequestBody Rider updatedRider) {
        try {
            return ResponseEntity
                    .ok(new APIResponse("Success", riderService.updateRider(riderId, updatedRider)));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{riderId}")
    public ResponseEntity<APIResponse> deleteRider(@PathVariable Long riderId) {
        try {
            riderService.deleteRider(riderId);
            return ResponseEntity.ok(new APIResponse("Successfully deleted", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/rc")
    public ResponseEntity<APIResponse> getCaptainsWithFeign() {
        try {
            return ResponseEntity.ok(new APIResponse("Success", fareAndDistanceClient.getCaptains()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }


}

