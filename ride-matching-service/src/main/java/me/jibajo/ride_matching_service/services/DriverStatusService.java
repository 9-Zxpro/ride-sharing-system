package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.enums.DriverStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverStatusService {
    private static final String DRIVER_STATUS_KEY = "driver_status";
    private final RedisTemplate<String, String> redisTemplate;

    public void updateDriverStatus(String driverId, DriverStatus status) {
        redisTemplate.opsForValue().set(
                DRIVER_STATUS_KEY + ":" + driverId,
                status.toString()
        );
    }

    public DriverStatus getDriverStatus(String driverId) {
        String status = redisTemplate.opsForValue().get(DRIVER_STATUS_KEY + ":" + driverId);
        return status != null ? DriverStatus.valueOf(status) : DriverStatus.AVAILABLE;
    }
}
