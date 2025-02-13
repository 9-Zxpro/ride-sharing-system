package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.dto.FoundCaptain;
import me.jibajo.ride_matching_service.enums.CaptainStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CaptainLocationService {
    private static final String CAPTAIN_ONLINE_KEY = "captains:available";
    private final RedisTemplate<String, Long> redisTemplate;

    @Value("${ride-matching.max-drivers-to-consider}")
    private final Long driver_limit;

    public List<FoundCaptain> findNearbyCaptains(double lat, double lng, double radiusKm) {
        Circle searchArea = new Circle(
                new Point(lat, lng),
                new Distance(radiusKm, Metrics.KILOMETERS)
        );

        return Objects.requireNonNull(redisTemplate.opsForGeo()
                        .radius(CAPTAIN_ONLINE_KEY, searchArea))
                .getContent()
                .stream()
                .map(geo -> new FoundCaptain(
                        geo.getContent().getName(),
                        geo.getContent().getPoint().getY(),
                        geo.getContent().getPoint().getX()
                ))
                .limit(driver_limit)
                .toList();
    }

//    public void updateDriverLocation(CaptainLocation location) {
//        redisTemplate.opsForGeo().add(
//                CAPTAIN_ONLINE_KEY,
//                new Point(location.longitude(), location.latitude()),
//                location.driverId()
//        );
//    }

}