package com.robust.study.netty.lion.common.message;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.tools.Utils;
import io.netty.buffer.ByteBuf;

import java.util.Map;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 17:07
 * @Version: 1.0
 */
public final class HttpRequestMessage extends ByteBufMessage {

    public byte method;
    public String uri;
    public Map<String, String> headers;
    public byte[] body;

    public HttpRequestMessage(Connection connection) {
        super(new Packet(Command.HTTP_PROXY, genSessionId()), connection);
    }

    public HttpRequestMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    protected void encode(ByteBuf body) {
        encodeByte(body, method);
        encodeString(body, uri);
        encodeString(body, Utils.headerToString(headers));
        encodeBytes(body, this.body);
    }

    @Override
    protected void decode(ByteBuf body) {
        method = decodeByte(body);
        uri = decodeString(body);
        headers = Utils.headerFromString(decodeString(body));
        this.body = decodeBytes(body);
    }

    public String getMethod() {
        switch (method) {
            case 0:
                return "GET";
            case 1:
                return "POST";
            case 2:
                return "PUT";
            case 3:
                return "DELETE";
        }
        return "GET";
    }

    @Override
    public String toString() {
        return "HttpRequestMessage{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", headers=" + headers +
                ", body=" + (body == null ? "" : body.length) +
                '}';
    }
}
