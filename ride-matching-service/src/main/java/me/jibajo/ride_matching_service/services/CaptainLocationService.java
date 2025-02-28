package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.dto.FoundCaptain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaptainLocationService {
    @Value("${captain-on-duty-key}")
    private String CAPTAIN_ON_DUTY_KEY;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CaptainLocationService.class);

    @Value("${ride-matching.max-drivers-to-consider}")
    private long driver_limit;

    public List<FoundCaptain> findNearbyCaptains(double lng, double lat, double radiusKm) {
        Circle searchArea = new Circle(
                new Point(lng, lat),
                new Distance(radiusKm, Metrics.KILOMETERS)
        );
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()  // Equivalent to WITHDIST
                .includeCoordinates(); // Equivalent to WITHCOORD


        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius(CAPTAIN_ON_DUTY_KEY, searchArea, args);

        if (geoResults == null || geoResults.getContent().isEmpty()) {
            return Collections.emptyList();
        }

        List<FoundCaptain> list = geoResults.getContent()
                .stream()
                .map(geo -> {
                    Object content = geo.getContent().getName();
                    if (content instanceof String captainIdStr) {
                        Long captainId = Long.parseLong(captainIdStr);
                        Point point = geo.getContent().getPoint();
                        return new FoundCaptain(captainId, point.getX(), point.getY());
                    }
                    throw new IllegalStateException("Unexpected content type in Redis Geo data");
                })
                .limit(driver_limit) // Unbox Long to primitive long
                .toList();

        for(FoundCaptain fc : list) {
            logger.info(String.valueOf(fc.captainId()));
        }

        return list;
    }

//    public void updateDriverLocation(CaptainLocation location) {
//        redisTemplate.opsForGeo().add(
//                CAPTAIN_ON_DUTY_KEY,
//                new Point(location.longitude(), location.latitude()),
//                location.driverId()
//        );
//    }

}