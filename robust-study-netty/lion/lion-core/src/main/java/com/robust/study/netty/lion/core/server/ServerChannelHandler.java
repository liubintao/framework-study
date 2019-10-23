package com.robust.study.netty.lion.core.server;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.connection.ConnectionManager;
import com.robust.study.netty.lion.api.event.ConnectionCloseEvent;
import com.robust.study.netty.lion.api.message.PacketReceiver;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.network.connection.NettyConnection;
import com.robust.study.netty.lion.tools.common.Profiler;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.event.EventBus;
import com.robust.study.netty.lion.tools.log.Logs;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/3 16:29
 * @Version: 1.0
 */
@ChannelHandler.Sharable
@Slf4j
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private static final long profile_slowly_limit = CC.lion.monitor.profile_slowly_duration.toMillis();
    private final boolean security;//是否启用加密
    private final ConnectionManager connectionManager;
    private final PacketReceiver receiver;

    public ServerChannelHandler(boolean security, ConnectionManager connectionManager, PacketReceiver receiver) {
        this.security = security;
        this.connectionManager = connectionManager;
        this.receiver = receiver;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logs.CONN.info("client connected conn={}", ctx.channel());
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), security);
        connectionManager.add(connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = (Packet) msg;
        byte cmd = packet.cmd;

        try {
            Profiler.start("time cost on [channel read]: ", packet.toString());
            Connection connection = connectionManager.get(ctx.channel());
            if (log.isDebugEnabled()) {
                log.debug("channelRead conn={}, packet={}", ctx.channel(), connection.getSessionContext(), packet);
            }
            connection.updateLastReadTime();
            receiver.onReceive(packet, connection);
        } finally {
            Profiler.release();

            if (Profiler.getDuration() > profile_slowly_limit) {
                Logs.PROFILE.info("Read Packet[cmd={}] Slowly: \n{}", Command.toCMD(cmd), Profiler.dump());
            }

            Profiler.reset();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        Logs.CONN.error("client caught ex, conn={}", connection);
        log.error("caught an ex, channel={}, conn={}", ctx.channel(), connection, cause);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = connectionManager.removeAndClose(ctx.channel());
        EventBus.post(new ConnectionCloseEvent(connection));
        Logs.CONN.info("client disconnected conn={}", connection);
    }
}
