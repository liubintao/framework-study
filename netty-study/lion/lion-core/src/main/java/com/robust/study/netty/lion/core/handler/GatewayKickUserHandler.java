package com.robust.study.netty.lion.core.handler;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.common.handler.BaseMessageHandler;
import com.robust.study.netty.lion.common.message.gateway.GatewayKickUserMessage;
import com.robust.study.netty.lion.core.router.RouterCenter;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 15:17
 * @Version: 1.0
 */
public final class GatewayKickUserHandler extends BaseMessageHandler<GatewayKickUserMessage> {

    private final RouterCenter routerCenter;

    public GatewayKickUserHandler(RouterCenter routerCenter) {
        this.routerCenter = routerCenter;
    }

    @Override
    public GatewayKickUserMessage decode(Packet packet, Connection connection) {
        return new GatewayKickUserMessage(packet, connection);
    }

    @Override
    public void handle(GatewayKickUserMessage message) {
        routerCenter.getRouterChangeListener().onReceiveKickRemoteMsg(message);
    }
}
