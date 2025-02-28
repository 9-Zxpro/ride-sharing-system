package me.jibajo.captain_service.services.captain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.CaptainOnDuty;
import me.jibajo.captain_service.enums.CaptainStatus;
import me.jibajo.captain_service.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CaptainStatusService {
    private static final String CAPTAIN_KEY_PREFIX = "captain:";
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CaptainStatusService.class);

    public Void createOrUpdateCaptainStatus(Long captainId, CaptainOnDuty captainOnDuty) {
        String key = CAPTAIN_KEY_PREFIX + captainId;

//        redisTemplate.opsForGeo().add(
//                "captains:onDuty",
//                new Point(captainOnDuty.lng(), captainOnDuty.lat()),
//                String.valueOf(captainId)
//        );
        logger.info("Captain ID before conversion: {}", captainId);
        String captainIdString = String.valueOf(captainId);
        logger.info("Captain ID after conversion: {}", captainIdString);
        redisTemplate.opsForGeo().add(
                "captains:onDuty",
                new Point(captainOnDuty.lng(), captainOnDuty.lat()),
                captainIdString
        );
        logger.info("Captain ID stored in Redis: {}", captainIdString);

        redisTemplate.opsForHash().putAll(key, Map.of(
                "captainId", String.valueOf(captainId),
                "status", captainOnDuty.status().name(),
                "lat", String.valueOf(captainOnDuty.lat()),
                "lng", String.valueOf(captainOnDuty.lng()),
                "lastUpdated", Instant.now().toString()
        ));

//        redisTemplate.expire("captains:onDuty", 30, TimeUnit.MINUTES);
//        redisTemplate.expire(key, 30, TimeUnit.MINUTES);

        return null;
    }

    public CaptainStatus getCaptainStatus(Long captainId) {
        String statusString = (String) redisTemplate.opsForHash().get(CAPTAIN_KEY_PREFIX + captainId, "status");

        if (statusString == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(statusString, CaptainStatus.class);
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
