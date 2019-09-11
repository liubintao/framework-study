

package com.robust.study.netty.lion.core.handler;


import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.message.MessageHandler;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.handler.PushHandlerFactory;
import com.robust.study.netty.lion.common.handler.BaseMessageHandler;
import com.robust.study.netty.lion.common.message.AckMessage;
import com.robust.study.netty.lion.common.message.PushMessage;
import com.robust.study.netty.lion.tools.log.Logs;

@SPI(order = 1)
public final class ClientPushHandler extends BaseMessageHandler<PushMessage> implements PushHandlerFactory {

    @Override
    public PushMessage decode(Packet packet, Connection connection) {
        return new PushMessage(packet, connection);
    }

    @Override
    public void handle(PushMessage message) {
        Logs.PUSH.info("receive client push message={}", message);

        if (message.autoAck()) {
            AckMessage.from(message).sendRaw();
            Logs.PUSH.info("send ack for push message={}", message);
        }
        //biz code write here
    }

    @Override
    public MessageHandler get() {
        return this;
    }
}
