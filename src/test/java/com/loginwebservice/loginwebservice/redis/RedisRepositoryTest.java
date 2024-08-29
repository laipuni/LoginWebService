package com.loginwebservice.loginwebservice.redis;

import com.loginwebservice.loginwebservice.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;


class RedisRepositoryTest extends IntegrationTest {

    @Autowired
    RedisRepository redisRepository;

    @Autowired
    StringRedisTemplate redisTemplate;

    @DisplayName("해당 키로 저장된 값을 조회한다.")
    @Test
    void getData(){
        //given
        String expectedKey = "예상키";
        String expectedValue = "예상값";

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedKey,expectedValue);
        //when
        String resultValue = redisRepository.getData(expectedKey);
        //then
        assertThat(resultValue).isEqualTo(expectedValue);
    }

    @DisplayName("해당 키로 저장된 값이 있는지 확인한다.")
    @Test
    void existData(){
        String expectedKey = "예상키";
        String expectedValue = "예상값";

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedKey,expectedValue);
        //when
        Boolean result = redisRepository.existData(expectedKey);
        //then
        assertThat(result).isTrue();
    }

    @DisplayName("키와 값을 지정된 시간 후 삭제된다.")
    @Test
    void setDataExpire() throws InterruptedException {
        String expectedKey = "예상키";
        String expectedValue = "예상값";
        long duration = 1;

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedKey,expectedValue);
        //when
        redisRepository.setDataExpire(expectedKey,expectedKey,duration);

        //then
        Thread.sleep(1100 * duration);
        Boolean result = redisTemplate.hasKey(expectedKey);
        assertThat(result).isFalse();
    }

    @DisplayName("key값을 받아 해당 키와 값를 삭제한다.")
    @Test
    void deleteData(){
        String expectedKey = "예상키";
        String expectedValue = "예상값";

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(expectedKey,expectedValue);
        //when
        redisRepository.deleteData(expectedKey);

        //then
        Boolean result = redisTemplate.hasKey(expectedKey);
        assertThat(result).isFalse();
    }
}