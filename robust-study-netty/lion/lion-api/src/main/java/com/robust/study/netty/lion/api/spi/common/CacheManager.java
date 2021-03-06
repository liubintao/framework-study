package com.robust.study.netty.lion.api.spi.common;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:57
 * @Version: 1.0
 */
public interface CacheManager {

    void init();

    void destroy();

    void del(String key);

    long hincrBy(String key, String field, long value);

    void set(String key, String value);

    void set(String key, String value, int expireTime);

    void set(String key, Object value, int expireTime);

    <T> T get(String key, Class<T> tClass);

    void hset(String key, String field, String value);

    void hset(String key, String field, Object value);

    <T> T hget(String key, String field, Class<T> tClass);

    void hdel(String key, String field);

    <T> Map<String, T> hgetAll(String key, Class<T> tClass);

    void zAdd(String key, String value);

    Long zCard(String key);

    void zRem(String key, String value);

    <T> List<T> zrange(String key, int start, int end, Class<T> tClass);

    void lpush(String key, String... value);

    <T> List<T> lrange(String key, int start, int end, Class<T> tClass);
}
