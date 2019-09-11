package com.robust.study.netty.lion.api.event;

import com.robust.study.netty.lion.api.connection.Connection;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 8:05
 * @Version: 1.0
 */
public final class ConnectionCloseEvent implements Event {
    public Connection connection;

    public ConnectionCloseEvent(Connection connection) {
        this.connection = connection;
    }
}
