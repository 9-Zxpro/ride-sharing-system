package me.jibajo.ride_matching_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.ride_matching_service.dto.DriverLocation;
import me.jibajo.ride_matching_service.enums.DriverStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DriverLocationService {
    private static final String DRIVER_LOCATION_KEY = "driver_locations";
    private final RedisTemplate<String, Long> redisTemplate;

    @Value("${ride-matching.max-drivers-to-consider}")
    private final Long driver_limit;

    public void updateDriverLocation(DriverLocation location) {
        redisTemplate.opsForGeo().add(
                DRIVER_LOCATION_KEY,
                new Point(location.longitude(), location.latitude()),
                location.driverId()
        );
    }

    public List<DriverLocation> findNearbyDrivers(double latitude, double longitude, double radiusKm) {
        Circle searchArea = new Circle(
                new Point(longitude, latitude),
                new Distance(radiusKm, Metrics.KILOMETERS)
        );

        return Objects.requireNonNull(redisTemplate.opsForGeo()
                        .radius(DRIVER_LOCATION_KEY, searchArea))
                .getContent()
                .stream()
                .map(geo -> new DriverLocation(
                        geo.getContent().getName(),
                        geo.getContent().getPoint().getY(),
                        geo.getContent().getPoint().getX(),
                        DriverStatus.AVAILABLE
                ))
                .limit(driver_limit)
                .toList();
    }
}