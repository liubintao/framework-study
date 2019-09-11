
package com.robust.study.netty.lion.api.event;


import com.robust.study.netty.lion.api.connection.Connection;

public final class UserOnlineEvent implements Event {

    private final Connection connection;
    private final String userId;

    public UserOnlineEvent(Connection connection, String userId) {
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