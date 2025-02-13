package me.jibajo.captain_service.services.captain;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.enums.CaptainStatus;
import me.jibajo.captain_service.exceptions.NotFoundException;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CaptainStatusService {
    private static final String CAPTAIN_KEY_PREFIX = "captain:";
    private final RedisTemplate<String, Object> redisTemplate;

    public Void createOrUpdateCaptainStatus(Long captainId, CaptainStatus status,
                                                           Double lat, Double lng) {
        String key = CAPTAIN_KEY_PREFIX + captainId;

        redisTemplate.opsForGeo().add(
                "captains:available",
                new Point(lng, lat),
                captainId
        );

        redisTemplate.opsForHash().putAll(key, Map.of(
                "captainId", captainId,
                "status", status,
                "lat", lat,
                "lng", lng,
                "lastUpdated", Instant.now().toString()
        ));

//        redisTemplate.opsForHash().put(key, "captainId", captainId.toString());
//        redisTemplate.opsForHash().put(key, "status", status.name());
//
//        if (lat != null && lng != null) {
//            redisTemplate.opsForHash().put(key, "lat", lat.toString());
//            redisTemplate.opsForHash().put(key, "lng", lng.toString());
//        }

        redisTemplate.expire(key, 5, TimeUnit.MINUTES);

//        Map<Object, Object> captainData = redisTemplate.opsForHash().entries(key);
//        Map<String, Object> responseData = new HashMap<>();
//        captainData.forEach((k, v) -> responseData.put(k.toString(), v));
//
//        if (responseData.containsKey("lat")) {
//            responseData.put("lat", Double.parseDouble((String) responseData.get("lat")));
//        }
//        if (responseData.containsKey("lng")) {
//            responseData.put("lng", Double.parseDouble((String) responseData.get("lng")));
//        }

        return null;
    }

    public CaptainStatus getCaptainStatus(Long captainId) {
        return (CaptainStatus) redisTemplate.opsForValue().get(CAPTAIN_KEY_PREFIX + captainId);
    }

    public void removeCaptain(Long captainId) {
        String key = CAPTAIN_KEY_PREFIX + captainId;

        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new NotFoundException("Captain with ID " + captainId + " not found in Redis.");
        }
        redisTemplate.delete(key);
    }

    public List<String> getAvailableCaptains() {
        Set<String> keys = redisTemplate.keys(CAPTAIN_KEY_PREFIX + "*");
        return keys.stream()
                .filter(key -> {
                    String status = (String) redisTemplate.opsForHash().get(key, "status");
                    return CaptainStatus.ONLINE.name().equals(status);
                })
                .map(key -> key.replace(CAPTAIN_KEY_PREFIX, ""))
                .toList();
    }

//    @Scheduled(fixedRate = 30000)
//    public void checkOnlineStatus() {
//        Set<String> keys = redisTemplate.keys(CAPTAIN_KEY_PREFIX + "*");
//        Instant now = Instant.now();
//
//        keys.forEach(key -> {
//            String lastOnlineStr = (String) redisTemplate.opsForHash().get(key, "last_online");
//            Instant lastOnline = Instant.parse(lastOnlineStr);
//
//            if (Duration.between(lastOnline, now).toMinutes() > 5) {
//                statusService.createOrUpdateCaptainStatus(
//                        key.replace(CAPTAIN_KEY_PREFIX, ""),
//                        CaptainStatus.OFFLINE,
//                        null,
//                        null
//                );
//            }
//        });
//    }

}
