
package com.robust.study.netty.lion.test.spi;

import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.CacheManager;
import com.robust.study.netty.lion.api.spi.common.CacheManagerFactory;

/**
 */
@SPI(order = 2)
public final class SimpleCacheMangerFactory implements CacheManagerFactory {
    @Override
    public CacheManager get() {
        return FileCacheManger.I;
    }
}
