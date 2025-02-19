package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.dto.FoundCaptain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CaptainLocationService {
    @Value("${captain-on-duty-key}")
    private String CAPTAIN_ON_DUTY_KEY;

    private final RedisTemplate<String, Long> redisTemplate;

    @Value("${ride-matching.max-drivers-to-consider}")
    private Long driver_limit;

    public List<FoundCaptain> findNearbyCaptains(double lat, double lng, double radiusKm) {
        Circle searchArea = new Circle(
                new Point(lat, lng),
                new Distance(radiusKm, Metrics.KILOMETERS)
        );

        return Objects.requireNonNull(redisTemplate.opsForGeo()
                        .radius(CAPTAIN_ON_DUTY_KEY, searchArea))
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
//                CAPTAIN_ON_DUTY_KEY,
//                new Point(location.longitude(), location.latitude()),
//                location.driverId()
//        );
//    }

}