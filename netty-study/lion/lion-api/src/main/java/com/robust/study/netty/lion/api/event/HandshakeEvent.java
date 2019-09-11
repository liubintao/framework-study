

package com.robust.study.netty.lion.api.event;


import com.robust.study.netty.lion.api.connection.Connection;

public final class HandshakeEvent implements Event {
    public final Connection connection;
    public final int heartbeat;

    public HandshakeEvent(Connection connection, int heartbeat) {
        this.connection = connection;
        this.heartbeat = heartbeat;
    }
}
