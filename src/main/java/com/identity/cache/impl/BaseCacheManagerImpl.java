package com.identity.cache.impl;

import com.identity.cache.BaseCacheManager;
import com.identity.utils.DataUtils;
import com.identity.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(value = "baseCacheManager")
@RequiredArgsConstructor
public class BaseCacheManagerImpl implements BaseCacheManager {
    private final RedisUtil<String> redisUtil;

    @Value("${app.cache.prefix}")
    private String keyPrefix;

    @PostConstruct
    void started() {
        keyPrefix = keyPrefix + ":";
    }


    @Override
    public <T> List<T> getList(String key, Class zClass) {
        String jsonString = redisUtil.getValue(keyPrefix + key);
        List<T> objects = new ArrayList<>();
        if (!DataUtils.isNullOrEmpty(jsonString)){
            objects = DataUtils.jsonToListThrow(jsonString, zClass, "error.common.get.cache");
        }
        return objects;
    }


    @Override
    public void delete(String key) {
        redisUtil.delValue(keyPrefix + key);
    }

    @Override
    public void setCache(String key, List objects) {
        redisUtil.putValueWithExpireTime(keyPrefix + key, DataUtils.objectToJson(objects), 30, TimeUnit.DAYS);
    }

    @Override
    public void setCache(String key, Object object, int time, TimeUnit timeUnit) {
        redisUtil.putValueWithExpireTime(keyPrefix + key, DataUtils.objectToJson(object), time, timeUnit);
    }

    @Override
    public void setCache(String key, Object object) {
        redisUtil.putValueWithExpireTime(keyPrefix + key, DataUtils.objectToJson(object), 30, TimeUnit.DAYS);
    }

    @Override
    public <T> Object getObject(String key, Class zClass) {
        String jsonString = redisUtil.getValue(keyPrefix + key);
        if (!DataUtils.isNullOrEmpty(jsonString)){
            return DataUtils.jsonToObject(jsonString, zClass);
        }
        return null;
    }

    @Override
    public void setString(String key, String object, int time, TimeUnit timeUnit) {
        redisUtil.putValueWithExpireTime(keyPrefix + key, object, time, timeUnit);
    }

    @Override
    public String getString(String key) {
        return redisUtil.getValue(keyPrefix + key);
    }
}
