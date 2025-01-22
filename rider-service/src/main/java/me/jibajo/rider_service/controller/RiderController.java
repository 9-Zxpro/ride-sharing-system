package me.jibajo.rider_service.controller;

import me.jibajo.rider_service.entities.Rider;
import me.jibajo.rider_service.services.IRiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final IRiderService riderService;

    public RiderController(IRiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping
    public ResponseEntity<Rider> createRider(@RequestBody Rider rider) {
        Rider createdRider = riderService.createRider(rider);
        return ResponseEntity.ok(createdRider);
    }

    @GetMapping("/{riderId}")
    public ResponseEntity<Rider> getRiderById(@PathVariable Long riderId) {
        Optional<Rider> rider = riderService.getRiderById(riderId);
        return rider.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Rider>> getAllRiders() {
        List<Rider> riders = riderService.getAllRiders();
        return ResponseEntity.ok(riders);
    }

    @PutMapping("/{riderId}")
    public ResponseEntity<Rider> updateRider(@PathVariable Long riderId, @RequestBody Rider updatedRider) {
        Rider rider = riderService.updateRider(riderId, updatedRider);
        return ResponseEntity.ok(rider);
    }

    @DeleteMapping("/{riderId}")
    public ResponseEntity<Void> deleteRider(@PathVariable Long riderId) {
        riderService.deleteRider(riderId);
        return ResponseEntity.noContent().build();
    }
}

