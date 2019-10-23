
package com.robust.study.netty.lion.client.gateway;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.service.Listener;
import com.robust.study.netty.lion.client.LionClient;
import com.robust.study.netty.lion.client.gateway.handler.GatewayErrorHandler;
import com.robust.study.netty.lion.client.gateway.handler.GatewayOKHandler;
import com.robust.study.netty.lion.common.MessageDispatcher;
import com.robust.study.netty.lion.network.udp.NettyUDPConnector;
import com.robust.study.netty.lion.network.udp.UDPChannelHandler;
import com.robust.study.netty.lion.tools.Utils;
import com.robust.study.netty.lion.tools.config.CC;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import static com.robust.study.netty.lion.common.MessageDispatcher.POLICY_LOG;


/**
 */
public final class GatewayUDPConnector extends NettyUDPConnector {

    private UDPChannelHandler channelHandler;
    private MessageDispatcher messageDispatcher;
    private LionClient lionClient;

    public GatewayUDPConnector(LionClient lionClient) {
        super(CC.lion.net.gateway_client_port);
        this.lionClient = lionClient;
        this.messageDispatcher = new MessageDispatcher(POLICY_LOG);
    }

    @Override
    public void init() {
        super.init();
        messageDispatcher.register(Command.OK, () -> new GatewayOKHandler(lionClient));
        messageDispatcher.register(Command.ERROR, () -> new GatewayErrorHandler(lionClient));
        channelHandler = new UDPChannelHandler(messageDispatcher);
        channelHandler.setMulticastAddress(Utils.getInetAddress(CC.lion.net.gateway_client_multicast));
        channelHandler.setNetworkInterface(Utils.getLocalNetworkInterface());
    }


    @Override
    public void stop(Listener listener) {
        super.stop(listener);
    }


    @Override
    protected void initOptions(Bootstrap b) {
        super.initOptions(b);
        b.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, true);
        b.option(ChannelOption.IP_MULTICAST_TTL, 255);
        if (CC.lion.net.snd_buf.gateway_client > 0)
            b.option(ChannelOption.SO_SNDBUF, CC.lion.net.snd_buf.gateway_client);
        if (CC.lion.net.rcv_buf.gateway_client > 0)
            b.option(ChannelOption.SO_RCVBUF, CC.lion.net.rcv_buf.gateway_client);
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public Connection getConnection() {
        return channelHandler.getConnection();
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }
}
