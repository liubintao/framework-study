package com.robust.study.netty.lion.api.protocol;

import com.robust.tools.kit.mapper.JsonMapper;
import com.robust.tools.kit.text.Charsets;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/3 15:06
 * @Version: 1.0
 */
public class JsonPacket extends Packet {

    public Map<String, Object> body;

    public JsonPacket() {
        super(Command.UNKNOWN);
        this.addFlag(FLAG_JSON_BODY);
    }

    public JsonPacket(Command cmd) {
        super(cmd);
        this.addFlag(FLAG_JSON_BODY);
    }

    public JsonPacket(Command cmd, int sessionId) {
        super(cmd, sessionId);
        this.addFlag(FLAG_JSON_BODY);
    }

    @Override
    public Map<String, Object> getBody() {
        return body;
    }

    public <T> void setBody(T body) {
        this.body = (Map<String, Object>) body;
    }

    @Override
    public int getBodyLength() {
        return super.getBodyLength();
    }

    @Override
    public Packet response(Command command) {
        return new JsonPacket(command, sessionId);
    }

    @Override
    public Object toFrame(Channel channel) {
        byte[] bytes = JsonMapper.INSTANCE.toJson(this).getBytes(Charsets.UTF_8);
        return new TextWebSocketFrame(Unpooled.wrappedBuffer(bytes));
    }

    @Override
    public String toString() {
        return "JsonPacket{" +
                "cmd=" + cmd +
                ", cc=" + cc +
                ", flags=" + flags +
                ", sessionId=" + sessionId +
                ", lrc=" + lrc +
                ", body=" + body +
                '}';
    }
}
