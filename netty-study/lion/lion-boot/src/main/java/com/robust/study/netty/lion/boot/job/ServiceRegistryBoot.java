package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.api.spi.common.ServiceRegistryFactory;
import com.robust.study.netty.lion.tools.log.Logs;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/10 17:57
 * @Version: 1.0
 */
public final class ServiceRegistryBoot extends BootJob {
    @Override
    protected void start() {
        Logs.Console.info("init service registry waiting for connected...");
        ServiceRegistryFactory.create().syncStart();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        ServiceRegistryFactory.create().syncStop();
        Logs.Console.info("service registry closed...");
    }
}
