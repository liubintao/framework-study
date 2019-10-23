package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.api.spi.net.DnsMappingManager;
import com.robust.study.netty.lion.core.LionServer;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 8:42
 * @Version: 1.0
 */
public final class HttpProxyBoot extends BootJob {

    private final LionServer lionServer;

    public HttpProxyBoot(LionServer lionServer) {
        this.lionServer = lionServer;
    }

    @Override
    protected void start() {
        lionServer.getHttpClient().syncStart();
        DnsMappingManager.create().start();

        startNext();
    }

    @Override
    protected void stop() {
        stopNext();
        lionServer.getHttpClient().syncStop();
        DnsMappingManager.create().stop();
    }
}
