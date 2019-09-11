
package com.robust.study.netty.lion.core.push;


import com.robust.study.netty.lion.api.LionContext;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.push.PushListener;
import com.robust.study.netty.lion.api.spi.push.PushListenerFactory;
import com.robust.study.netty.lion.common.ErrorCode;
import com.robust.study.netty.lion.common.message.ErrorMessage;
import com.robust.study.netty.lion.common.message.OkMessage;
import com.robust.study.netty.lion.common.message.gateway.GatewayPushMessage;
import com.robust.study.netty.lion.common.push.GatewayPushResult;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.tools.Jsons;
import com.robust.study.netty.lion.tools.log.Logs;

import java.util.concurrent.ScheduledExecutorService;


@SPI(order = 1)
public final class GatewayPushListener implements PushListener<GatewayPushMessage>, PushListenerFactory<GatewayPushMessage> {

    private PushCenter pushCenter;

    @Override
    public void init(LionContext context) {
        pushCenter = ((LionServer) context).getPushCenter();
    }

    @Override
    public void onSuccess(GatewayPushMessage message, Object[] timePoints) {
        if (message.getConnection().isConnected()) {
            pushCenter.addTask(new PushTask() {
                @Override
                public ScheduledExecutorService getExecutor() {
                    return message.getExecutor();
                }

                @Override
                public void run() {
                    OkMessage
                            .from(message)
                            .setData(GatewayPushResult.toJson(message, timePoints))
                            .sendRaw();
                }
            });
        } else {
            Logs.PUSH.warn("push message to client success, but gateway connection is closed, timePoints={}, message={}"
                    , Jsons.toJson(timePoints), message);
        }
    }

    @Override
    public void onAckSuccess(GatewayPushMessage message, Object[] timePoints) {
        if (message.getConnection().isConnected()) {
            pushCenter.addTask(new PushTask() {
                @Override
                public ScheduledExecutorService getExecutor() {
                    return message.getExecutor();
                }

                @Override
                public void run() {
                    OkMessage
                            .from(message)
                            .setData(GatewayPushResult.toJson(message, timePoints))
                            .sendRaw();
                }
            });

        } else {
            Logs.PUSH.warn("client ack success, but gateway connection is closed, timePoints={}, message={}"
                    , Jsons.toJson(timePoints), message);
        }
    }

    @Override
    public void onBroadcastComplete(GatewayPushMessage message, Object[] timePoints) {
        if (message.getConnection().isConnected()) {
            pushCenter.addTask(new PushTask() {
                @Override
                public ScheduledExecutorService getExecutor() {
                    return message.getExecutor();
                }

                @Override
                public void run() {
                    OkMessage
                            .from(message)
                            .sendRaw();
                }
            });
        } else {
            Logs.PUSH.warn("broadcast to client finish, but gateway connection is closed, timePoints={}, message={}"
                    , Jsons.toJson(timePoints), message);
        }
    }

    @Override
    public void onFailure(GatewayPushMessage message, Object[] timePoints) {
        if (message.getConnection().isConnected()) {
            pushCenter.addTask(new PushTask() {
                @Override
                public ScheduledExecutorService getExecutor() {
                    return message.getExecutor();
                }

                @Override
                public void run() {
                    ErrorMessage
                            .from(message)
                            .setErrorCode(ErrorCode.PUSH_CLIENT_FAILURE)
                            .setData(GatewayPushResult.toJson(message, timePoints))
                            .sendRaw();
                }
            });
        } else {
            Logs.PUSH.warn("push message to client failure, but gateway connection is closed, timePoints={}, message={}"
                    , Jsons.toJson(timePoints), message);
        }
    }

    @Override
    public void onOffline(GatewayPushMessage message, Object[] timePoints) {
        if (message.getConnection().isConnected()) {
            pushCenter.addTask(new PushTask() {
                @Override
                public ScheduledExecutorService getExecutor() {
                    return message.getExecutor();
                }

                @Override
                public void run() {
                    ErrorMessage
                            .from(message)
                            .setErrorCode(ErrorCode.OFFLINE)
                            .setData(GatewayPushResult.toJson(message, timePoints))
                            .sendRaw();
                }
            });
        } else {
            Logs.PUSH.warn("push message to client offline, but gateway connection is closed, timePoints={}, message={}"
                    , Jsons.toJson(timePoints), message);
        }
    }

    @Override
    public void onRedirect(GatewayPushMessage message, Object[] timePoints) {
        if (message.getConnection().isConnected()) {
            pushCenter.addTask(new PushTask() {
                @Override
                public ScheduledExecutorService getExecutor() {
                    return message.getExecutor();
                }

                @Override
                public void run() {
                    ErrorMessage
                            .from(message)
                            .setErrorCode(ErrorCode.ROUTER_CHANGE)
                            .setData(GatewayPushResult.toJson(message, timePoints))
                            .sendRaw();
                }
            });
        } else {
            Logs.PUSH.warn("push message to client redirect, but gateway connection is closed, timePoints={}, message={}"
                    , Jsons.toJson(timePoints), message);
        }
    }


    @Override
    public void onTimeout(GatewayPushMessage message, Object[] timePoints) {
        Logs.PUSH.warn("push message to client timeout, timePoints={}, message={}"
                , Jsons.toJson(timePoints), message);
    }

    @Override
    public PushListener<GatewayPushMessage> get() {
        return this;
    }
}
