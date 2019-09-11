package com.robust.study.netty.lion.core.handler;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.common.handler.BaseMessageHandler;
import com.robust.study.netty.lion.common.message.AckMessage;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.core.ack.AckTask;
import com.robust.study.netty.lion.core.ack.AckTaskQueue;
import com.robust.study.netty.lion.tools.log.Logs;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 16:59
 * @Version: 1.0
 */
public class AckHandler extends BaseMessageHandler<AckMessage> {

    private final AckTaskQueue ackTaskQueue;

    public AckHandler(LionServer lionServer) {
        this.ackTaskQueue = lionServer.getPushCenter().getAckTaskQueue();
    }

    @Override
    public AckMessage decode(Packet packet, Connection connection) {
        return new AckMessage(packet, connection);
    }

    @Override
    public void handle(AckMessage message) {
        AckTask task = ackTaskQueue.getAndRemove(message.getSessionId());
        if (task == null) {//ack 超时了
            Logs.PUSH.info("receive client ack, but task timeout message={}", message);
            return;
        }

        task.onResponse();//成功收到客户的ACK响应
    }
}
