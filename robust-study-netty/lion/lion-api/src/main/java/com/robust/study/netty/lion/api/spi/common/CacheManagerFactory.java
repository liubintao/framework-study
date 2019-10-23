package com.robust.study.netty.lion.api.spi.common;

import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/5 17:01
 * @Version: 1.0
 */
public interface CacheManagerFactory extends Factory<CacheManager> {

    static CacheManager create() {
        return SpiLoader.load(CacheManagerFactory.class).get();
    }
}
