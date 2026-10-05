package com.smartstock.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RateLimitService(
            RedisTemplate<String, Object> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(
            Long userId,
            String resource,
            int maxRequests,
            long windowSeconds) {

        String key =
                "rate_limit:user:" + userId + ":" + resource;

        Long count =
                redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(
                    key,
                    windowSeconds,
                    TimeUnit.SECONDS
            );
        }

        return count <= maxRequests;

    }
}