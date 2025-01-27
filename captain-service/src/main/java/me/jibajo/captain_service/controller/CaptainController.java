package me.jibajo.captain_service.controller;

import me.jibajo.captain_service.exceptions.NotFoundException;
import me.jibajo.captain_service.requests.CaptainRegRequest;
import me.jibajo.captain_service.responses.APIResponse;
import me.jibajo.captain_service.services.captain.ICaptainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/captains")
public class CaptainController {

    private final ICaptainService captainService;

    @Autowired
    public CaptainController(ICaptainService captainService) {
        this.captainService = captainService;
    }

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
}

