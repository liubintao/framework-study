package com.robust.study.netty.lion.api.connection;

import io.netty.channel.Channel;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 11:14
 * @Version: 1.0
 */
public interface ConnectionManager {

    Connection get(Channel channel);

    Connection removeAndClose(Channel channel);

    void add(Connection connection);

    int getConnNum();

    void init();

    void destroy();
}
