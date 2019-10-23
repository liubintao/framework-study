package com.robust.study.netty.lion.core;

import com.robust.study.netty.lion.api.LionContext;
import com.robust.study.netty.lion.api.spi.common.*;
import com.robust.study.netty.lion.api.srd.ServiceDiscovery;
import com.robust.study.netty.lion.api.srd.ServiceNode;
import com.robust.study.netty.lion.api.srd.ServiceRegistry;
import com.robust.study.netty.lion.common.ServerNodes;
import com.robust.study.netty.lion.core.push.PushCenter;
import com.robust.study.netty.lion.core.router.RouterCenter;
import com.robust.study.netty.lion.core.server.*;
import com.robust.study.netty.lion.core.session.ReusableSessionManager;
import com.robust.study.netty.lion.monitor.service.MonitorService;
import com.robust.study.netty.lion.network.http.HttpClient;
import com.robust.study.netty.lion.network.http.NettyHttpClient;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.event.EventBus;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 15:32
 * @Version: 1.0
 */
public final class LionServer implements LionContext {

    private ServiceNode connServerNode;
    private ServiceNode gatewayServerNode;
    private ServiceNode webSocketServerNode;

    private ConnectionServer connectionServer;
    private WebSocketServer webSocketServer;
    private GatewayServer gatewayServer;
    private AdminServer adminServer;
    private GatewayUDPConnector udpGatewayServer;

    private HttpClient httpClient;

    private PushCenter pushCenter;

    private ReusableSessionManager reusableSessionManager;

    private RouterCenter routerCenter;

    private MonitorService monitorService;


    public LionServer() {
        connServerNode = ServerNodes.cs();
        gatewayServerNode = ServerNodes.gs();
        webSocketServerNode = ServerNodes.ws();

        monitorService = new MonitorService();
        EventBus.create(monitorService.getThreadPoolManager().getEventBusExecutor());

        reusableSessionManager = new ReusableSessionManager();

        pushCenter = new PushCenter(this);

        routerCenter = new RouterCenter(this);

        connectionServer = new ConnectionServer(this);

        webSocketServer = new WebSocketServer(this);

        adminServer = new AdminServer(this);

        if (CC.lion.net.tcpGateway()) {
            gatewayServer = new GatewayServer(this);
        } else {
            udpGatewayServer = new GatewayUDPConnector(this);
        }
    }

    public boolean isTargetMachine(String host, int port) {
        return port == gatewayServerNode.getPort() && gatewayServerNode.getHost().equals(host);
    }

    public ServiceNode getConnServerNode() {
        return connServerNode;
    }

    public ServiceNode getGatewayServerNode() {
        return gatewayServerNode;
    }

    public ServiceNode getWebSocketServerNode() {
        return webSocketServerNode;
    }

    public GatewayServer getGatewayServer() {
        return gatewayServer;
    }

    public AdminServer getAdminServer() {
        return adminServer;
    }

    public GatewayUDPConnector getUdpGatewayServer() {
        return udpGatewayServer;
    }

    public WebSocketServer getWebSocketServer() {
        return webSocketServer;
    }

    public ConnectionServer getConnectionServer() {
        return connectionServer;
    }

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    httpClient = new NettyHttpClient();
                }
            }
        }
        return httpClient;
    }

    public PushCenter getPushCenter() {
        return pushCenter;
    }

    public ReusableSessionManager getReusableSessionManager() {
        return reusableSessionManager;
    }

    public RouterCenter getRouterCenter() {
        return routerCenter;
    }

    @Override
    public MonitorService getMonitor() {
        return monitorService;
    }

    @Override
    public ServiceDiscovery getDiscovery() {
        return ServiceDiscoveryFactory.create();
    }

    @Override
    public ServiceRegistry getRegistry() {
        return ServiceRegistryFactory.create();
    }

    @Override
    public CacheManager getCacheManager() {
        return CacheManagerFactory.create();
    }

    @Override
    public MQClient getMQClient() {
        return MQClientFactory.create();
    }


}
