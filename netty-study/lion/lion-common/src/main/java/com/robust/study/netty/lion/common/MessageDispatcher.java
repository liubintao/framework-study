package com.robust.study.netty.lion.common;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.message.MessageHandler;
import com.robust.study.netty.lion.api.message.PacketReceiver;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.common.message.ErrorMessage;
import com.robust.study.netty.lion.tools.common.Profiler;
import com.robust.study.netty.lion.tools.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.robust.study.netty.lion.common.ErrorCode.DISPATCH_ERROR;
import static com.robust.study.netty.lion.common.ErrorCode.UNSUPPORTED_CMD;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 11:24
 * @Version: 1.0
 */
@Slf4j
public final class MessageDispatcher implements PacketReceiver {

    public static final int POLICY_REJECT = 2;
    public static final int POLICY_LOG = 1;
    public static final int POLICY_IGNORE = 0;
    private final int unsupportedPolicy;

    private final Map<Byte, MessageHandler> handlers = new HashMap<>();

    public MessageDispatcher() {
        this.unsupportedPolicy = POLICY_REJECT;
    }

    public MessageDispatcher(int unsupportedPolicy) {
        this.unsupportedPolicy = unsupportedPolicy;
    }

    public void register(Command command, MessageHandler handler) {
        handlers.put(command.cmd, handler);
    }

    public void register(Command command, Supplier<MessageHandler> supplier, boolean enabled) {
        if (enabled && !handlers.containsKey(command.cmd)) {
            register(command, supplier.get());
        }
    }

    public void register(Command command, Supplier<MessageHandler> supplier) {
        register(command, supplier, true);
    }

    public MessageHandler unregister(Command command) {
        return handlers.remove(command.cmd);
    }

    @Override
    public void onReceive(Packet packet, Connection connection) {
        MessageHandler handler = handlers.get(packet.cmd);
        if (handler != null) {
            Profiler.enter("time cost on [dispatcher]");
            try {
                handler.handle(packet, connection);
            } catch (Throwable throwable) {
                log.error("dispatch message ex, packet={}, connect={}, body={}"
                        , packet, connection, Arrays.toString(packet.body), throwable);
                Logs.CONN.error("dispatch message ex, packet={}, connect={}, body={}, error={}"
                        , packet, connection, Arrays.toString(packet.body), throwable.getMessage());
                ErrorMessage
                        .from(packet, connection)
                        .setErrorCode(DISPATCH_ERROR)
                        .close();
            } finally {
                Profiler.release();
            }
        } else {
            if (unsupportedPolicy > POLICY_IGNORE) {
                Logs.CONN.error("dispatch message failure, cmd={} unsupported, packet={}, connect={}, body={}"
                        , Command.toCMD(packet.cmd), packet, connection);
                if (unsupportedPolicy == POLICY_REJECT) {
                    ErrorMessage
                            .from(packet, connection)
                            .setErrorCode(UNSUPPORTED_CMD)
                            .close();
                }
            }
        }
    }
}
