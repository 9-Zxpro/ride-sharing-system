package me.jibajo.ride_management_service.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {
    private final RedisTemplate<String, String> redisTemplate;

    public OtpService(@Qualifier("otpRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public String generateOtp(Long rideId) {
        String otp = String.format("%04d", new Random().nextInt(10000));

        redisTemplate.opsForValue().set(
                "otp:" + rideId,
                otp
        );

        return otp;
    }
}