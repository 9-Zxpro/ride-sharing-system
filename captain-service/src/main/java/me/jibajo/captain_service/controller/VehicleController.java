package me.jibajo.captain_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.VehicleDTO;
import me.jibajo.captain_service.entities.Vehicle;
import me.jibajo.captain_service.exceptions.NotFoundException;
import me.jibajo.captain_service.responses.APIResponse;
import me.jibajo.captain_service.services.vehicle.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final IVehicleService vehicleService;

    @GetMapping("/captain/{captainId}")
    public ResponseEntity<APIResponse> getVehicleByCaptainId(@PathVariable Long captainId) {
        try {
            VehicleDTO vehicleDTO = vehicleService.getVehicleByCaptainId(captainId);
            return ResponseEntity.ok(new APIResponse("Success", vehicleDTO));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/update/{captainId}")
    public ResponseEntity<APIResponse> addOrUpdateVehicle(@PathVariable Long captainId,
                                                          @RequestBody VehicleDTO vehicleDTO) {
        try {
            Vehicle updatedVehicle = vehicleService.addOrUpdateVehicle(captainId, vehicleDTO);
            return ResponseEntity.ok(new APIResponse("Vehicle added/updated successfully", updatedVehicle));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
    }
}

