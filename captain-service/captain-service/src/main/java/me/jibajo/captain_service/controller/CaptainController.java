package me.jibajo.captain_service.controller;

import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.services.ICaptainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/captains")
public class CaptainController {

    private final ICaptainService captainService;

    public CaptainController(ICaptainService captainService) {
        this.captainService = captainService;
    }

    @PostMapping
    public ResponseEntity<Captain> createCaptain(@RequestBody Captain captain) {
        return ResponseEntity.ok(captainService.createCaptain(captain));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Captain> getCaptainById(@PathVariable Long id) {
        return captainService.getCaptainById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Captain>> getAllCaptains() {
        return ResponseEntity.ok(captainService.getAllCaptains());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Captain> updateCaptain(@PathVariable Long id, @RequestBody Captain updatedCaptain) {
        return ResponseEntity.ok(captainService.updateCaptain(id, updatedCaptain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCaptain(@PathVariable Long id) {
        captainService.deleteCaptain(id);
        return ResponseEntity.noContent().build();
    }
}

