package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.common.ErrorCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 11:59
 * @Version: 1.0
 */
@ToString
public class ErrorMessage extends ByteBufMessage {

    public byte cmd;
    public byte code;
    public String reason;
    public String data;

    public ErrorMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    public ErrorMessage(byte cmd, Packet packet, Connection connection) {
        super(packet, connection);
        this.cmd = cmd;
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeByte(body, cmd);
        encodeByte(body, code);
        encodeString(body, reason);
        encodeString(body, data);
    }

    @Override
    protected void decode(ByteBuf body) {
        cmd = decodeByte(body);
        cmd = decodeByte(body);
        reason = decodeString(body);
        data = decodeString(body);
    }

    @Override
    protected Map<String, Object> encodeJsonBody() {
        Map<String, Object> body = new HashMap<>(4);
        body.put("cmd", cmd);
        body.put("code", code);
        body.put("reason", reason);
        body.put("data", data);
        return body;
    }

    @Override
    public void send(ChannelFutureListener listener) {
        super.sendRaw(listener);
    }

    @Override
    public void close() {
        sendRaw(ChannelFutureListener.CLOSE);
    }

    public static ErrorMessage from(BaseMessage src) {
        return new ErrorMessage(src.packet.cmd, src.packet.response(Command.ERROR), src.connection);
    }

    public static ErrorMessage from(Packet packet, Connection connection) {
        return new ErrorMessage(packet.cmd, packet.response(Command.ERROR), connection);
    }

    public ErrorMessage setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ErrorMessage setData(String data) {
        this.data = data;
        return this;
    }

    public ErrorMessage setErrorCode(ErrorCode code) {
        this.code = code.errorCode;
        this.reason = code.errorMsg;
        return this;
    }
}
