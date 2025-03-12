package com.loginwebservice.loginwebservice.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.Callable;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRepository {

    private final StringRedisTemplate template;

    public String getData(String key) {
        return (String) handleException(() -> {
            ValueOperations<String, String> operations = template.opsForValue();
            return operations.get(key);
        });
    }

    public Boolean existData(String key) {
        return (Boolean) handleException(() -> Boolean.TRUE.equals(template.hasKey(key)));
    }

    public void setDataExpire(String key, String value, long duration) {
        handleException(() -> {
            ValueOperations<String, String> operations = template.opsForValue();
            Duration expireDuration = Duration.ofSeconds(duration);
            operations.set(key, value, expireDuration);
            return null;
        });
    }

    public Boolean deleteData(String key) {
        return (Boolean) handleException(() -> template.delete(key));
    }

    private Object handleException(Callable<?> callable) {
        try {
            return callable.call();
        } catch (RedisConnectionFailureException e) {
            throw new IllegalStateException("서버에 문제가 생겨 해당 서비스를 이용할 수 없습니다. 죄송합니다.", e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
