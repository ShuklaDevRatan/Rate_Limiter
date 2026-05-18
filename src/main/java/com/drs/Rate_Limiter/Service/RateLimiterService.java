package com.drs.Rate_Limiter.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${rate.limit}")
    private int rateLimit;

    @Value("${rate.duration}")
    private int rateDuration;

    public Boolean getCount(String ip) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - (rateDuration * 60 * 1000L);
        redisTemplate.opsForZSet()
                .removeRangeByScore(
                        ip,
                        0,
                        windowStart
                );
        Long requestCount = redisTemplate.opsForZSet().zCard(ip);
        if (requestCount != null && requestCount >= rateLimit) {
            return false;
        }
        redisTemplate.opsForZSet()
                .add(
                        ip,
                        String.valueOf(currentTime),
                        (double) currentTime
                );

        return true;
    }
}