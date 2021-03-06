
package com.robust.study.netty.lion.common.handler;


import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.message.MessageHandler;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.common.message.BaseMessage;
import com.robust.study.netty.lion.tools.common.Profiler;

/**
 */
public abstract class BaseMessageHandler<T extends BaseMessage> implements MessageHandler {


    public abstract T decode(Packet packet, Connection connection);

    public abstract void handle(T message);

    @Override
    public void handle(Packet packet, Connection connection) {
        Profiler.enter("time cost on [message decode]");
        T t = decode(packet, connection);
        if (t != null) t.decodeBody();
        Profiler.release();

        if (t != null) {
            Profiler.enter("time cost on [handle]");
            handle(t);
            Profiler.release();
        }
    }

    /*@SuppressWarnings("unchecked")
    private final Class<T> mClass = Reflects.getSuperClassGenericType(this.getClass(), 0);

    protected T decode0(Packet packet, Connection connection) {
        if (packet.hasFlag(Packet.FLAG_JSON_STRING_BODY)) {
            String body = packet.getBody();
            T t = Jsons.fromJson(body, mClass);
            if (t != null) {
                t.setConnection(connection);
                t.setPacket(packet);
            }
            return t;
        }
        return decode(packet, connection);
    }*/
}
