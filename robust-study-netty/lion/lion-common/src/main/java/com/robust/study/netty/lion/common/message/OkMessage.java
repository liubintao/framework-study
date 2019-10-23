package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 15:14
 * @Version: 1.0
 */
public final class OkMessage extends ByteBufMessage {

    public byte cmd;
    public byte code;
    public String data;

    public OkMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    public OkMessage(byte cmd, Packet packet, Connection connection) {
        super(packet, connection);
        this.cmd = cmd;
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeByte(body, cmd);
        encodeByte(body, code);
        encodeString(body, data);
    }

    @Override
    protected void decode(ByteBuf body) {
        this.cmd = decodeByte(body);
        this.code = decodeByte(body);
        this.data = decodeString(body);
    }

    @Override
    public Map<String, Object> encodeJsonBody() {
        Map<String, Object> body = new HashMap<>(3);
        if (cmd > 0) body.put("cmd", cmd);
        if (code > 0) body.put("code", code);
        if (data != null) body.put("data", data);
        return body;
    }

    public static OkMessage from(BaseMessage message) {
        return new OkMessage(message.packet.cmd, message.packet.response(Command.OK), message.connection);
    }

    public OkMessage setCode(byte code) {
        this.code = code;
        return this;
    }

    public OkMessage setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "OkMessage{" +
                "data='" + data + '\'' +
                "packet='" + packet + '\'' +
                '}';
    }
}
