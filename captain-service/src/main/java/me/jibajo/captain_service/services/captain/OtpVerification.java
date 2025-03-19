package me.jibajo.captain_service.services.captain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpVerification {
    private final RedisTemplate<String, String> otpRedisTemplate;

    public boolean validateOtp(Long rideId, String enteredOtp) {
        String storedOtp = otpRedisTemplate.opsForValue().get("otp:" + rideId);
        if (storedOtp != null && storedOtp.equals(enteredOtp)) {
            otpRedisTemplate.delete("otp:" + rideId);
            return true;
        }
        return false;
    }
}
