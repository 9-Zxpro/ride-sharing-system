package me.jibajo.ride_matching_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ride-matching")
@Data
public class RideMatchingProperties {
    private double searchRadius;
    private int maxDriversToConsider;
}
