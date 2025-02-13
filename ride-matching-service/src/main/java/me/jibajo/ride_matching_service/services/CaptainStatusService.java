package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.enums.CaptainStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptainStatusService {
    private static final String CAPTAIN_KEY_PREFIX = "captain:";
    private final RedisTemplate<String, String> redisTemplate;

    public void updateDriverStatus(String driverId, CaptainStatus status) {
        redisTemplate.opsForValue().set(
                CAPTAIN_KEY_PREFIX + ":" + driverId,
                status.toString()
        );
    }

    public CaptainStatus getDriverStatus(String driverId) {
        String status = redisTemplate.opsForValue().get(CAPTAIN_KEY_PREFIX + ":" + driverId);
        return status != null ? CaptainStatus.valueOf(status) : CaptainStatus.ON_RIDE;
    }
}
