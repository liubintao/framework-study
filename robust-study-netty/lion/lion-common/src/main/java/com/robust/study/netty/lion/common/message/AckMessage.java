package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 17:00
 * @Version: 1.0
 */
public final class AckMessage extends BaseMessage {

    public AckMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected byte[] encode() {
        return null;
    }

    @Override
    public void decode(byte[] body) {

    }

    public static AckMessage from(BaseMessage src) {
        return new AckMessage(new Packet(Command.ACK, src.getSessionId()), src.connection);
    }

    @Override
    public String toString() {
        return "AckMessage{" +
                "packet=" + packet +
                '}';
    }
}
