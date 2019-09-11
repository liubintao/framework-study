package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 14:26
 * @Version: 1.0
 */
public final class BindUserMessage extends ByteBufMessage {

    public String userId;
    public String tags;
    public String data;

    public BindUserMessage(Connection connection) {
        super(new Packet(Command.BIND, genSessionId()), connection);
    }

    public BindUserMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeString(body, userId);
        encodeString(body, tags);
        encodeString(body, data);
    }

    @Override
    protected void decode(ByteBuf body) {
        this.userId = decodeString(body);
        this.tags = decodeString(body);
        this.data = decodeString(body);
    }

    @Override
    public String toString() {
        return "BindUserMessage{" +
                "data='" + data + '\'' +
                ", userId='" + userId + '\'' +
                ", tags='" + tags + '\'' +
                ", packet=" + packet +
                '}';
    }
}
