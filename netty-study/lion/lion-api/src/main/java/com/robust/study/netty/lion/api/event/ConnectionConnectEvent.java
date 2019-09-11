

package com.robust.study.netty.lion.api.event;


import com.robust.study.netty.lion.api.connection.Connection;

public final class ConnectionConnectEvent implements Event {
    public final Connection connection;

    public ConnectionConnectEvent(Connection connection) {
        this.connection = connection;
    }
}
