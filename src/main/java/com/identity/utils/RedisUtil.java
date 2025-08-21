package com.identity.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RedisUtil<T> {
    private final RedisTemplate<String, T> redisTemplate;

    public GeoOperations<String, T> opsForGeo() {
        return redisTemplate.opsForGeo();
    }


    public void putMap(String redisKey, Object key, T data) {
        redisTemplate.opsForHash().put(redisKey, key, data);
    }

    public void putValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putValueWithExpireTime(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public T getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public Boolean delValue(String key) {
        return redisTemplate.delete(key);
    }

    public void boundHashOps(String redisKey, String key, T value) {
        redisTemplate.opsForHash().put(redisKey, key, value);
    }

    public List<Object> getBoundHashOps(String redisKey) {
        return redisTemplate.boundHashOps(redisKey).values();
    }
}
