package me.jibajo.captain_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.APIResponse;
import me.jibajo.captain_service.dto.CaptainRegRequest;
import me.jibajo.captain_service.enums.CaptainStatus;
import me.jibajo.captain_service.exceptions.NotFoundException;
import me.jibajo.captain_service.services.captain.CaptainStatusService;
import me.jibajo.captain_service.services.captain.ICaptainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/captains")
@RequiredArgsConstructor
public class CaptainController {

    private final ICaptainService captainService;
    private final CaptainStatusService captainStatusService;

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

    @PostMapping("/{id}/onDuty")
    public ResponseEntity<APIResponse> onlineCaptain(@PathVariable long captainId,
                                                     @RequestParam CaptainStatus status,
                                                     @RequestParam double lat,
                                                     @RequestParam double lng) {
        try {
            captainService.createCaptainQueue(captainId);

            return ResponseEntity.ok(new APIResponse("Captain is Online",
                    captainStatusService.createOrUpdateCaptainStatus(
                            captainId,
                            status,
                            lat,
                            lng)));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/offDuty")
    public ResponseEntity<APIResponse> offlineCaptain(@PathVariable Long id) {
        try {
            captainService.deleteCaptainQueue(id);

            captainStatusService.removeCaptain(id);
            return ResponseEntity.ok(new APIResponse("Captain goes offline Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{captainId}/accept/{rideId}")
    public ResponseEntity<APIResponse> acceptRide(@PathVariable Long captainId,
                                                  @PathVariable Long rideId) {
        APIResponse response = captainService.acceptRide(rideId, captainId);
        return ResponseEntity.ok(response);
    }
}

