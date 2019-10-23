package com.robust.study.netty.lion.api.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Packet;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/3 17:05
 * @Version: 1.0
 */
public interface PacketReceiver {
    void onReceive(Packet packet, Connection connection);
}
