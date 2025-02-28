package me.jibajo.captain_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.APIResponse;
import me.jibajo.captain_service.dto.CaptainOnDuty;
import me.jibajo.captain_service.dto.CaptainRegRequest;
import me.jibajo.captain_service.exceptions.NotFoundException;
import me.jibajo.captain_service.services.captain.CaptainStatusService;
import me.jibajo.captain_service.services.captain.ICaptainService;
import me.jibajo.captain_service.services.feignclient.RideManagerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/captains")
@RequiredArgsConstructor
public class CaptainController {

    private final ICaptainService captainService;
    private final CaptainStatusService captainStatusService;
    private final RideManagerClient rideManagerClient;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> createCaptain(@RequestBody CaptainRegRequest captainRegRequest) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.createCaptain(captainRegRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCaptainById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.getCaptainById(id)));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllCaptains() {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.getAllCaptains()));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }


//    TODO: Make it patchMapping to update individually
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateCaptain(@PathVariable Long id, @RequestBody CaptainRegRequest updatedCaptain) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.updateCaptain(id, updatedCaptain)));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCaptain(@PathVariable Long id) {
        try {
            captainService.deleteCaptain(id);
            return ResponseEntity.ok(new APIResponse("Captain deleted Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{captainId}/onDuty")
    public ResponseEntity<APIResponse> onlineCaptain(@PathVariable Long captainId,
                                                     @RequestBody CaptainOnDuty captainOnDuty) {
        try {
            return ResponseEntity.ok(new APIResponse("Captain is Online",
                    captainStatusService.createOrUpdateCaptainStatus( captainId, captainOnDuty)));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/onDuty/{captainId}/status")
    public ResponseEntity<APIResponse> getOnDutyCaptainStatus(@PathVariable Long captainId) {
        try {
            return ResponseEntity.ok(new APIResponse("Success",
                    captainStatusService.getCaptainStatus(captainId)));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/offDuty")
    public ResponseEntity<APIResponse> offlineCaptain(@PathVariable Long id) {
        try {
            captainStatusService.removeCaptain(id);
            return ResponseEntity.ok(new APIResponse("Captain goes offline Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{captainId}/accept/{rideId}")
    public ResponseEntity<APIResponse> acceptRide(@PathVariable Long captainId,
                                                  @PathVariable Long rideId) {
//        APIResponse response = captainService.acceptRide(rideId, captainId);
        APIResponse response = rideManagerClient.acceptedRideOffer(rideId, captainId);
        return ResponseEntity.ok(response);
    }
}

