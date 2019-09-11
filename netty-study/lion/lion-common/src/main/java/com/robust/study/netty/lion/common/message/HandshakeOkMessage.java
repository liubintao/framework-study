package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 11:48
 * @Version: 1.0
 */
public final class HandshakeOkMessage extends ByteBufMessage {

    public byte[] serverKey;
    public int heartbeat;
    public String sessionId;
    public long expireTime;

    public HandshakeOkMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeBytes(body, serverKey);
        encodeInt(body, heartbeat);
        encodeString(body, sessionId);
        encodeLong(body, expireTime);
    }

    @Override
    protected void decode(ByteBuf body) {
        serverKey = decodeBytes(body);
        heartbeat = decodeInt(body);
        sessionId = decodeString(body);
        expireTime = decodeLong(body);
    }

    public static HandshakeOkMessage from(BaseMessage message) {
        return new HandshakeOkMessage(message.packet.response(Command.HANDSHAKE), message.connection);
    }

    public HandshakeOkMessage setServerKey(byte[] serverKey) {
        this.serverKey = serverKey;
        return this;
    }

    public HandshakeOkMessage setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        return this;
    }

    public HandshakeOkMessage setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public HandshakeOkMessage setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    @Override
    public String toString() {
        return "HandshakeOkMessage{" +
                "expireTime=" + expireTime +
                ", serverKey=" + Arrays.toString(serverKey) +
                ", heartbeat=" + heartbeat +
                ", sessionId='" + sessionId + '\'' +
                ", packet=" + packet +
                '}';
    }
}
