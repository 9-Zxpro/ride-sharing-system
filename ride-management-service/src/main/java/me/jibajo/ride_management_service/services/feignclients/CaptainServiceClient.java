package me.jibajo.ride_management_service.services.feignclients;

import me.jibajo.ride_management_service.dto.APIResponse;
import me.jibajo.ride_management_service.dto.GeoPoint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "captain-service") // Assuming your Captain service name is "captain-service"
public interface CaptainServiceClient {

    @PostMapping("/api/captains/{captainId}/status-change-on-ride")
    APIResponse updateCaptainStatus(@PathVariable Long captainId, @RequestBody GeoPoint geoPoint);

    @PostMapping("/api/captains/{captainId}/status-change-to-available")
    APIResponse updateCaptainStatusToONDUTY(@PathVariable Long captainId);
}