package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.api.spi.common.ServiceDiscoveryFactory;
import com.robust.study.netty.lion.tools.log.Logs;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/10 18:00
 * @Version: 1.0
 */
public final class ServiceDiscoveryBoot extends BootJob {
    @Override
    protected void start() {
        Logs.Console.info("init service discovery waiting for connected...");
        ServiceDiscoveryFactory.create().syncStart();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        ServiceDiscoveryFactory.create().syncStop();
        Logs.Console.info("service discovery closed...");
    }
}
