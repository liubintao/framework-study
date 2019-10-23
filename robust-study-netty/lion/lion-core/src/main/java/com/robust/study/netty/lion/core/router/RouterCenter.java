
package com.robust.study.netty.lion.core.router;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.event.RouterChangeEvent;
import com.robust.study.netty.lion.api.router.ClientLocation;
import com.robust.study.netty.lion.api.router.Router;
import com.robust.study.netty.lion.api.service.BaseService;
import com.robust.study.netty.lion.api.service.Listener;
import com.robust.study.netty.lion.common.router.RemoteRouter;
import com.robust.study.netty.lion.common.router.RemoteRouterManager;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.tools.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class RouterCenter extends BaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterCenter.class);

    private LocalRouterManager localRouterManager;
    private RemoteRouterManager remoteRouterManager;
    private UserEventConsumer userEventConsumer;
    private RouterChangeListener routerChangeListener;
    private LionServer lionServer;

    public RouterCenter(LionServer lionServer) {
        this.lionServer = lionServer;
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        localRouterManager = new LocalRouterManager();
        remoteRouterManager = new RemoteRouterManager();
        routerChangeListener = new RouterChangeListener(lionServer);
        userEventConsumer = new UserEventConsumer(remoteRouterManager);
        userEventConsumer.getUserManager().clearOnlineUserList();
        super.doStart(listener);
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        userEventConsumer.getUserManager().clearOnlineUserList();
        super.doStop(listener);
    }

    /**
     * 注册用户和链接
     *
     * @param userId
     * @param connection
     * @return
     */
    public boolean register(String userId, Connection connection) {
        ClientLocation location = ClientLocation
                .from(connection)
                .setHost(lionServer.getGatewayServerNode().getHost())
                .setPort(lionServer.getGatewayServerNode().getPort());

        LocalRouter localRouter = new LocalRouter(connection);
        RemoteRouter remoteRouter = new RemoteRouter(location);

        LocalRouter oldLocalRouter = null;
        RemoteRouter oldRemoteRouter = null;
        try {
            oldLocalRouter = localRouterManager.register(userId, localRouter);
            oldRemoteRouter = remoteRouterManager.register(userId, remoteRouter);
        } catch (Exception e) {
            LOGGER.error("register router ex, userId={}, connection={}", userId, connection, e);
        }

        if (oldLocalRouter != null) {
            EventBus.post(new RouterChangeEvent(userId, oldLocalRouter));
            LOGGER.info("register router success, find old local router={}, userId={}", oldLocalRouter, userId);
        }

        if (oldRemoteRouter != null && oldRemoteRouter.isOnline()) {
            EventBus.post(new RouterChangeEvent(userId, oldRemoteRouter));
            LOGGER.info("register router success, find old remote router={}, userId={}", oldRemoteRouter, userId);
        }
        return true;
    }

    public boolean unRegister(String userId, int clientType) {
        localRouterManager.unRegister(userId, clientType);
        remoteRouterManager.unRegister(userId, clientType);
        return true;
    }

    public Router<?> lookup(String userId, int clientType) {
        LocalRouter local = localRouterManager.lookup(userId, clientType);
        if (local != null) return local;
        RemoteRouter remote = remoteRouterManager.lookup(userId, clientType);
        return remote;
    }

    public LocalRouterManager getLocalRouterManager() {
        return localRouterManager;
    }

    public RemoteRouterManager getRemoteRouterManager() {
        return remoteRouterManager;
    }

    public RouterChangeListener getRouterChangeListener() {
        return routerChangeListener;
    }

    public UserEventConsumer getUserEventConsumer() {
        return userEventConsumer;
    }
}
