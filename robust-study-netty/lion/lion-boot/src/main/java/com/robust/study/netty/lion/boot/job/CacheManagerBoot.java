package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.api.spi.common.CacheManagerFactory;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/10 17:53
 * @Version: 1.0
 */
public final class CacheManagerBoot extends BootJob {
    @Override
    protected void start() {
        CacheManagerFactory.create().init();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        CacheManagerFactory.create().destroy();
    }
}
