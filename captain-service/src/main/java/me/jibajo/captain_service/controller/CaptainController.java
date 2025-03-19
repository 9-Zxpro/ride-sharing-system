package me.jibajo.captain_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.*;
import me.jibajo.captain_service.enums.CaptainStatus;
import me.jibajo.captain_service.exceptions.NotFoundException;
import me.jibajo.captain_service.services.captain.CaptainStatusService;
import me.jibajo.captain_service.services.captain.ICaptainService;
import me.jibajo.captain_service.services.captain.OtpVerification;
import me.jibajo.captain_service.services.feignclient.RideManagerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/captains")
@RequiredArgsConstructor
public class CaptainController {

    private final ICaptainService captainService;
    private final CaptainStatusService captainStatusService;
    private final RideManagerClient rideManagerClient;
    private final OtpVerification otpVerification;

    private static final Logger logger = LoggerFactory.getLogger(CaptainController.class);

    @PostMapping("/add")
    public ResponseEntity<APIResponse> createCaptain(@RequestBody CaptainRegRequest captainRegRequest) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.createCaptain(captainRegRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCaptainById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.getCaptainById(id)));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllCaptains() {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.getAllCaptains()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }


//    TODO: Make it patchMapping to update individually
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateCaptain(@PathVariable Long id, @RequestBody CaptainRegRequest updatedCaptain) {
        try {
            return ResponseEntity.ok(new APIResponse("Success", captainService.updateCaptain(id, updatedCaptain)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteCaptain(@PathVariable Long id) {
        try {
            captainService.deleteCaptain(id);
            return ResponseEntity.ok(new APIResponse("Captain deleted Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{captainId}/onDuty")
    public ResponseEntity<APIResponse> onlineCaptain(@PathVariable Long captainId,
                                                     @RequestBody CaptainStatusCache captainStatusCache) {
        try {
            return ResponseEntity.ok(new APIResponse("Captain is Online",
                    captainStatusService.createOnDutyCaptainCache( captainId, captainStatusCache)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/onDuty/{captainId}/status")
    public ResponseEntity<APIResponse> getOnDutyCaptainStatus(@PathVariable Long captainId) {
        try {
            return ResponseEntity.ok(new APIResponse("Success",
                    captainStatusService.getCaptainStatus(captainId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/offDuty")
    public ResponseEntity<APIResponse> offlineCaptain(@PathVariable Long id) {
        try {
            boolean isRemoved = captainStatusService.removeCaptain(id);
            return ResponseEntity.ok(new APIResponse("Captain goes offline Successfully", isRemoved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{captainId}/status-change-on-ride")
    public ResponseEntity<APIResponse> updateCaptainStatus(@PathVariable Long captainId, @RequestBody GeoPoint geoPoint) {
        try {
            CaptainStatusCache captainStatusCache = new CaptainStatusCache(CaptainStatus.ON_RIDE, geoPoint);
            boolean updated = captainStatusService.updateOnDutyCaptainCache(captainId, captainStatusCache);
            return ResponseEntity.ok(new APIResponse("updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/api/captains/{captainId}/status-change-to-available")
    public ResponseEntity<APIResponse> updateCaptainStatusToOnDuty(@PathVariable Long captainId) {
        try {
            boolean updated = captainStatusService.updateCaptainStatus(captainId, CaptainStatus.ON_DUTY);
            return ResponseEntity.ok(new APIResponse("updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/{captainId}/accept/{rideId}")
    public ResponseEntity<APIResponse> acceptRide(@PathVariable Long captainId,
                                                  @PathVariable Long rideId) {
//        APIResponse response = captainService.acceptRide(rideId, captainId);
        try {
            CaptainDTO captainDTO = captainService.getCaptainById(captainId);
            APIResponse response = rideManagerClient.acceptedRideOffer(rideId, captainId, captainDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/verifyOtp/{rideId}")
    public ResponseEntity<APIResponse> verifyOtp(@PathVariable Long rideId, @RequestBody OtpDTO otpDTO) {
        try {
            boolean isVerified = otpVerification.validateOtp(rideId, otpDTO.otp());
            if (isVerified) {
                return ResponseEntity.ok(new APIResponse("OTP verified successfully", true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new APIResponse("Invalid OTP", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/{captainId}/startRiding/{rideId}")
    public ResponseEntity<APIResponse> startRide(@PathVariable Long rideId,
                                                 @PathVariable Long captainId,
                                                 @RequestBody CaptainStatusCache captainStatusCache) {
        try {
            APIResponse rideUpdated;
            try {
                rideUpdated = rideManagerClient.startRideProcess(rideId, captainId, captainStatusCache.point());
                logger.info("ride manager client called {}", rideUpdated);
            } catch (Exception e) {
                rideUpdated = new APIResponse("Ride update failed", false);
            }
            Boolean rideUpdatedData = (Boolean) rideUpdated.getData();
            if (Boolean.TRUE.equals(rideUpdatedData)) {
                return ResponseEntity.ok(new APIResponse("Ride started successfully." + rideUpdated, true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new APIResponse("Failed to start ride or update captain status.", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Failed to start ride", false));
        }
    }

    @PostMapping("/{captainId}/rideCompleted/{rideId}")
    public ResponseEntity<APIResponse> rideCompleted(@PathVariable Long rideId,
                                                 @PathVariable Long captainId) {
        try {
            APIResponse rideUpdated;
            try {
                rideUpdated = rideManagerClient.rideCompleted(rideId, captainId);
            } catch (Exception e) {
                rideUpdated = new APIResponse("Ride update failed", false);
            }
            Boolean rideUpdatedData = (Boolean) rideUpdated.getData();
            if (Boolean.TRUE.equals(rideUpdatedData)) {
                return ResponseEntity.ok(new APIResponse("Ride Completed successfully.", true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new APIResponse("Failed to complete ride.", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("Failed to complete ride", false));
        }
    }
}


