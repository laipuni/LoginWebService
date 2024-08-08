package com.loginwebservice.loginwebservice.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate template;

    public String getData(String key){
        ValueOperations<String, String> operations = template.opsForValue();
        return operations.get(key);
    }

    public Boolean existData(String key){
        return template.hasKey(key);
    }

    public void setDataExpire(String key, String value, long duration){
        ValueOperations<String, String> operations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        operations.set(key, value, expireDuration);
    }

    public void deleteData(String key){
        template.delete(key);
    }

}
