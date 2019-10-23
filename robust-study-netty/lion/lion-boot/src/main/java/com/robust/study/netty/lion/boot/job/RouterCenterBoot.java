package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.core.LionServer;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 8:39
 * @Version: 1.0
 */
public final class RouterCenterBoot extends BootJob {

    private final LionServer lionServer;

    public RouterCenterBoot(LionServer lionServer) {
        this.lionServer = lionServer;
    }

    @Override
    protected void start() {
        lionServer.getRouterCenter().start();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        lionServer.getRouterCenter().stop();
    }
}
