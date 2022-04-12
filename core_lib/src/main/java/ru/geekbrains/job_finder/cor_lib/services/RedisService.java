package ru.geekbrains.job_finder.cor_lib.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;

    public void saveToken(String token) {
        redisTemplate.opsForValue().set("token:"+ token,1, Duration.ofDays(1));
    }

    public Boolean checkTokenInRedis(String token) {
        return redisTemplate.opsForValue().get("token:" + token) != null;
    }
}
