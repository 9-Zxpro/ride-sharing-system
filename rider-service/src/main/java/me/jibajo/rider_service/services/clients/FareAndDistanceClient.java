package me.jibajo.rider_service.services.clients;

import me.jibajo.rider_service.dto.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "captain-service", url = "${ride-management-service.url}")
public interface FareAndDistanceClient {

    @GetMapping
    public ResponseEntity<APIResponse> getCaptains();
}
