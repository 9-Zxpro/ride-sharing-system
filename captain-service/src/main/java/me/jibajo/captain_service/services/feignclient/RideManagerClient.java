package me.jibajo.captain_service.services.feignclient;

import me.jibajo.captain_service.dto.APIResponse;
import me.jibajo.captain_service.dto.CaptainDTO;
import me.jibajo.captain_service.dto.GeoPoint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//, path = "http://localhost:9090/api/rides/"
@FeignClient(name = "ride-management-service")
public interface RideManagerClient {

    @PostMapping("/api/rides/{rideId}/accept/{captainId}")
    APIResponse acceptedRideOffer(@PathVariable("rideId") Long rideId,
                                  @PathVariable("captainId") Long captainId,
                                  @RequestBody CaptainDTO captainDTO);

    @PostMapping("/api/rides/{captainId}/start/{rideId}")
    APIResponse startRideProcess(@PathVariable("rideId") Long rideId,
                                 @PathVariable("captainId") Long captainId,
                                 @RequestBody GeoPoint geoPoint);

    @PostMapping("/api/rides/{captainId}/ride-completed/{rideId}")
    APIResponse rideCompleted(@PathVariable("rideId") Long rideId, @PathVariable("captainId") Long captainId);
}
