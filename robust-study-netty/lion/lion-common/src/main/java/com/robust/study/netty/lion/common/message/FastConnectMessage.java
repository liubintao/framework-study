package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 16:03
 * @Version: 1.0
 */
public final class FastConnectMessage extends ByteBufMessage {

    public String sessionId;
    public String deviceId;
    public int minHeartbeat;
    public int maxHeartbeat;

    public FastConnectMessage(Connection connection) {
        super(new Packet(Command.FAST_CONNECT, genSessionId()), connection);
    }

    public FastConnectMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeString(body, sessionId);
        encodeString(body, deviceId);
        encodeInt(body, minHeartbeat);
        encodeInt(body, maxHeartbeat);
    }

    @Override
    protected void decode(ByteBuf body) {
        sessionId = decodeString(body);
        deviceId = decodeString(body);
        minHeartbeat = decodeInt(body);
        maxHeartbeat = decodeInt(body);
    }

    @Override
    public String toString() {
        return "FastConnectMessage{" +
                "deviceId='" + deviceId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", minHeartbeat=" + minHeartbeat +
                ", maxHeartbeat=" + maxHeartbeat +
                ", packet=" + packet +
                '}';
    }
}
