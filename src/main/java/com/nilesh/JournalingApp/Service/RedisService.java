package com.nilesh.JournalingApp.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilesh.JournalingApp.api.response.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);

            if (o == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);

        } catch (JsonProcessingException e) {
            log.error("Exception", e);
            return null;
        }
    }
    public void set(String key, Object o, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String JsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, JsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }
}
