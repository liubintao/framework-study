
package com.robust.study.netty.lion.api.event;


import com.robust.study.netty.lion.api.connection.Connection;

public final class UserOfflineEvent implements Event {

    private final Connection connection;
    private final String userId;

    public UserOfflineEvent(Connection connection, String userId) {
        this.connection = connection;
        this.userId = userId;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getUserId() {
        return userId;
    }
}
