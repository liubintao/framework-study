package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.core.LionServer;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 8:41
 * @Version: 1.0
 */
public final class PushCenterBoot extends BootJob {

    private final LionServer lionServer;

    public PushCenterBoot(LionServer lionServer) {
        this.lionServer = lionServer;
    }

    @Override
    protected void start() {
        lionServer.getPushCenter().start();
        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        lionServer.getPushCenter().stop();
    }
}
