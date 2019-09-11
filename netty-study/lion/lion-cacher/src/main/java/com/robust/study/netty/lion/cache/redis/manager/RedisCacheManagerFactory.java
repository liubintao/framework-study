package com.robust.study.netty.lion.cache.redis.manager;

import com.robust.study.netty.lion.api.spi.common.CacheManager;
import com.robust.study.netty.lion.api.spi.common.CacheManagerFactory;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 15:44
 * @Version: 1.0
 */
public final class RedisCacheManagerFactory implements CacheManagerFactory {

    @Override
    public CacheManager get() {
        return RedisManager.I;
    }
}
