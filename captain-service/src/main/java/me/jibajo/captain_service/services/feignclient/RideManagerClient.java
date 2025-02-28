package me.jibajo.captain_service.services.feignclient;

import me.jibajo.captain_service.dto.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ride-management-service", path = "http://localhost:9090/api/rides/")
public interface RideManagerClient {

    @PostMapping("/{rideId}/accept/{captainId}")
    APIResponse acceptedRideOffer(@PathVariable("rideId") Long rideId, @PathVariable("captainId") Long captainId);
}
