package com.robust.study.netty.lion.api.connection;

import com.robust.study.netty.lion.api.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 9:30
 * @Version: 1.0
 */
public interface Connection {
    byte STATE_NEW = 0;
    byte STATE_CONNECTED = 1;
    byte STATE_DISCONNECTED = 2;

    void init(Channel channel, boolean security);

    SessionContext getSessionContext();

    void setSessionContext(SessionContext sessionContext);

    ChannelFuture send(Packet packet);

    ChannelFuture send(Packet packet, ChannelFutureListener listener);

    String getId();

    ChannelFuture close();

    boolean isConnected();

    boolean isReadTimeout();

    boolean isWriteTimeout();

    void updateLastWriteTime();

    void updateLastReadTime();

    Channel getChannel();
}
