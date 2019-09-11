package com.robust.study.netty.lion.api.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Packet;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 11:26
 * @Version: 1.0
 */
public interface MessageHandler {
    void handle(Packet packet, Connection connection);
}
