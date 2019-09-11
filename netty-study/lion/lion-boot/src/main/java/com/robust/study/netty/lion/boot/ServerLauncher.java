package com.robust.study.netty.lion.boot;

import com.robust.study.netty.lion.api.common.ServerEventListener;
import com.robust.study.netty.lion.api.spi.core.ServerEventListenerFactory;
import com.robust.study.netty.lion.boot.job.*;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.tools.config.CC;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 19:57
 * @Version: 1.0
 */
public final class ServerLauncher {
    private LionServer lionServer;
    private BootChain chain;
    private ServerEventListener serverEventListener;

    public void init() {
        if (lionServer == null) {
            lionServer = new LionServer();
        }

        if (chain == null) {
            chain = BootChain.chain();
        }

        if (serverEventListener == null) {
            serverEventListener = ServerEventListenerFactory.create();
        }

        serverEventListener.init(lionServer);

        chain.boot()
                .setNext(new CacheManagerBoot())//1.初始化缓存模块
                .setNext(new ServiceRegistryBoot())//2.初始化服务注册与发现模块
                .setNext(new ServiceDiscoveryBoot())//2.初始化服务注册与发现模块
                .setNext(new ServerBoot(lionServer.getConnectionServer(), lionServer.getConnServerNode()))//3.启动接入服务
                .setNext(() -> new ServerBoot(lionServer.getWebSocketServer(), lionServer.getWebSocketServerNode()), CC.lion.net.wsEnabled())//4.启动websocket接入服务
                .setNext(() -> new ServerBoot(lionServer.getUdpGatewayServer(), lionServer.getGatewayServerNode()), CC.lion.net.udpGateway())//5.启动udp网关服务
                .setNext(() -> new ServerBoot(lionServer.getGatewayServer(), lionServer.getGatewayServerNode()), CC.lion.net.tcpGateway())//6.启动tcp网关服务
                .setNext(new ServerBoot(lionServer.getAdminServer(), null))//7.启动控制台服务
                .setNext(new RouterCenterBoot(lionServer))//8.启动路由中心组件
                .setNext(new PushCenterBoot(lionServer))//9.启动推送中心组件
                .setNext(() -> new HttpProxyBoot(lionServer), CC.lion.http.proxy_enabled)//10.启动http代理服务，dns解析服务
                .setNext(new MonitorBoot(lionServer))//11.启动监控服务
                .end();
    }

    public void start() {
        chain.start();
    }

    public void stop() {
        chain.stop();
    }

    public void setLionServer(LionServer lionServer) {
        this.lionServer = lionServer;
    }

    public void setChain(BootChain chain) {
        this.chain = chain;
    }

    public LionServer getLionServer() {
        return lionServer;
    }

    public BootChain getChain() {
        return chain;
    }

    public ServerEventListener getServerEventListener() {
        return serverEventListener;
    }

    public void setServerEventListener(ServerEventListener serverEventListener) {
        this.serverEventListener = serverEventListener;
    }
}
