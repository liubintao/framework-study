package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.core.LionServer;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 8:48
 * @Version: 1.0
 */
public final class MonitorBoot extends BootJob {

    private final LionServer lionServer;

    public MonitorBoot(LionServer lionServer) {
        this.lionServer = lionServer;
    }

    @Override
    protected void start() {
        lionServer.getMonitor().start();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        lionServer.getMonitor().stop();
    }
}
