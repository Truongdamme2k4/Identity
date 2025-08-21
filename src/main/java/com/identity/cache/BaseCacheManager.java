package com.identity.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface BaseCacheManager {
    <T> List<T> getList(String key, Class zClass);

    void delete(String key);

    void setCache(String key, List objects);

    void setCache(String key, Object object, int time, TimeUnit timeUnit);

    void setCache(String key, Object object);

    <T> Object getObject(String key, Class zClass);

    void setString(String key, String object, int time, TimeUnit timeUnit);
    String getString(String key);
}
