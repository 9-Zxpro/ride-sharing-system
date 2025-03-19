package me.jibajo.captain_service.services.captain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.APIResponse;
import me.jibajo.captain_service.dto.CaptainStatusCache;
import me.jibajo.captain_service.dto.GeoPoint;
import me.jibajo.captain_service.enums.CaptainStatus;
import me.jibajo.captain_service.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CaptainStatusService {
    private static final String CAPTAIN_KEY_PREFIX = "captain:";
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${onDuty-captain-key}")
    private String onDutyCaptainKey; // "captains:onDuty"

    @Value("${onRide-captain-key}")
    private String onRideCaptainKey; // "captains:onRide"

    private static final Logger logger = LoggerFactory.getLogger(CaptainStatusService.class);

    public APIResponse createOnDutyCaptainCache(Long captainId, CaptainStatusCache captainStatusCache) {
        String key = CAPTAIN_KEY_PREFIX + captainId;

//        redisTemplate.opsForGeo().add(
//                "captains:onDuty",
//                new Point(captainOnDuty.lng(), captainOnDuty.lat()),
//                String.valueOf(captainId)
//        );
        String captainIdString = String.valueOf(captainId);
        redisTemplate.opsForGeo().add(
                onDutyCaptainKey,
                new Point(captainStatusCache.point().longitude(), captainStatusCache.point().latitude()),
                captainIdString
        );
        logger.info("Captain ID stored in Redis: {}", captainIdString);


//        redisTemplate.expire(onDutyCaptainKey, 30, TimeUnit.MINUTES);
        return new APIResponse("Captain cached", null);
    }

    // changing to onRide
    public boolean updateOnDutyCaptainCache(Long captainId, CaptainStatusCache captainStatusCache) {
        boolean isRemoved = removeCaptain(captainId);
        if(isRemoved) {
            boolean isUpdated = updateCaptainCache(captainId, captainStatusCache);
            logger.info("Captain status updated to onRide in Redis cache");
            return isUpdated;
        }
        return false;
    }

    public boolean updateCaptainCache(Long captainId, CaptainStatusCache captainStatusCache) {
        try {
            String key = CAPTAIN_KEY_PREFIX + captainId;
            redisTemplate.opsForHash().putAll(key, Map.of(
                    "captainId", String.valueOf(captainId),
                    "status", captainStatusCache.status().name(),
                    "lat", String.valueOf(captainStatusCache.point().latitude()),
                    "lng", String.valueOf(captainStatusCache.point().longitude()),
                    "lastUpdated", Instant.now().toString()
            ));
//        redisTemplate.expire(key, 30, TimeUnit.MINUTES);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateCaptainStatus(Long captainId, CaptainStatus newStatus) {
        try {
            String key = CAPTAIN_KEY_PREFIX + captainId;
            redisTemplate.opsForHash().put(key, "status", newStatus.name());

            CaptainStatusCache cache = getCaptainCache(captainId);
            createOnDutyCaptainCache(captainId, cache);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CaptainStatus getCaptainStatus(Long captainId) {
        String statusString = (String) redisTemplate.opsForHash().get(CAPTAIN_KEY_PREFIX + captainId, "status");

        if (statusString == null) {
            return null;
        }
        try {
            return CaptainStatus.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public CaptainStatusCache getCaptainCache(Long captainId) {
        String key = CAPTAIN_KEY_PREFIX + captainId;
        Map<Object, Object> captainData = redisTemplate.opsForHash().entries(key);

        if (captainData.isEmpty()) {
            return null;
        }

        try {
            CaptainStatus status = CaptainStatus.valueOf((String) captainData.get("status"));
            double lat = Double.parseDouble((String) captainData.get("lat"));
            double lng = Double.parseDouble((String) captainData.get("lng"));
            GeoPoint geoPoint = new GeoPoint(lng, lat);

            return new CaptainStatusCache(status, geoPoint);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean removeCaptain(Long captainId) {
        String onDutyCaptainKey = "captains:onDuty";
        String captainIdString = String.valueOf(captainId);
        try {
            boolean exists = redisTemplate.opsForZSet().score(onDutyCaptainKey, captainIdString) != null;

            if (!exists) {
                throw new NotFoundException("Captain with ID " + captainIdString + " not found in Geo index.");
            }

            Long removedCount = redisTemplate.opsForGeo().remove(onDutyCaptainKey, captainIdString);
            return removedCount != null && removedCount > 0;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAvailableCaptains() {
        Set<String> keys = redisTemplate.keys(CAPTAIN_KEY_PREFIX + "*");
        return keys.stream()
                .filter(key -> {
                    String status = (String) redisTemplate.opsForHash().get(key, "status");
                    return CaptainStatus.ON_DUTY.name().equals(status);
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
