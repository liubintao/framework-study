package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.api.service.Listener;
import com.robust.study.netty.lion.api.service.Server;
import com.robust.study.netty.lion.api.spi.common.ServiceRegistryFactory;
import com.robust.study.netty.lion.api.srd.ServiceNode;
import com.robust.study.netty.lion.tools.log.Logs;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/10 18:12
 * @Version: 1.0
 */
public final class ServerBoot extends BootJob {

    private final Server server;
    private final ServiceNode node;

    public ServerBoot(Server server, ServiceNode node) {
        this.server = server;
        this.node = node;
    }

    @Override
    protected void start() {
        server.init();
        server.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                Logs.Console.info("start {} success on:{}", server.getClass().getSimpleName(), args[0]);
                if (node != null) {//注册应用到zk
                    ServiceRegistryFactory.create().register(node);
                    Logs.RSD.info("register {} to srd success.", node);
                }
                startNext();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Logs.Console.error("start {} failure, jvm exit with code -1", server.getClass().getSimpleName(), throwable);
                System.exit(-1);
            }
        });
    }

    @Override
    protected void stop() {
        stopNext();
        if (node != null) {
            ServiceRegistryFactory.create().deregister(node);
        }
        Logs.Console.info("try shutdown {}...", server.getClass().getSimpleName());
        server.stop().join();
        Logs.Console.info("{} shutdown success.", server.getClass().getSimpleName());
    }

    @Override
    protected String getName() {
        return super.getName() + '(' + server.getClass().getSimpleName() + ')';
    }
}
