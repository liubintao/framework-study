package com.robust.study.netty.lion.core.handler;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.connection.SessionContext;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.common.ErrorCode;
import com.robust.study.netty.lion.common.handler.BaseMessageHandler;
import com.robust.study.netty.lion.common.message.ErrorMessage;
import com.robust.study.netty.lion.common.message.HandshakeMessage;
import com.robust.study.netty.lion.common.message.HandshakeOkMessage;
import com.robust.study.netty.lion.common.security.AesCipher;
import com.robust.study.netty.lion.common.security.CipherBox;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.core.session.ReusableSession;
import com.robust.study.netty.lion.core.session.ReusableSessionManager;
import com.robust.study.netty.lion.tools.config.ConfigTools;
import com.robust.study.netty.lion.tools.log.Logs;
import com.robust.tools.kit.text.StringUtil;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/5 10:37
 * @Version: 1.0
 */
public final class HandshakeHandler extends BaseMessageHandler<HandshakeMessage> {

    private final ReusableSessionManager reusableSessionManager;

    public HandshakeHandler(LionServer lionServer) {
        this.reusableSessionManager = lionServer.getReusableSessionManager();
    }

    @Override
    public HandshakeMessage decode(Packet packet, Connection connection) {
        return new HandshakeMessage(packet, connection);
    }

    @Override
    public void handle(HandshakeMessage message) {
        if (message.getConnection().getSessionContext().isSecurity()) {
            doSecurity(message);
        } else {
            doInSecurity(message);
        }
    }

    private void doSecurity(HandshakeMessage message) {
        byte[] iv = message.iv;//AES密钥向量16位
        byte[] clientKey = message.clientKey;//客户端随机数16位
        byte[] serverKey = CipherBox.I.randomAESKey();//服务端随机数16位
        byte[] sessionKey = CipherBox.I.mixKey(clientKey, serverKey);//会话密钥16位

        //1.校验客户端消息字段
        if (StringUtil.isBlank(message.deviceId)
                || iv.length != CipherBox.I.getAesKeyLength()
                || clientKey.length != CipherBox.I.getAesKeyLength()) {
            ErrorMessage.from(message).setReason("param invalid").close();
            Logs.CONN.error("handshake failure, message={}, conn={}", message, message.getConnection());
            return;
        }
        //2.重复握手判断
        SessionContext context = message.getConnection().getSessionContext();
        if (message.deviceId.equals(context.deviceId)) {
            ErrorMessage.from(message).setErrorCode(ErrorCode.REPEAT_HANDSHAKE).close();
            Logs.CONN.warn("handshake failure, repeat handshake, conn={}", message.getConnection());
            return;
        }
        //3.更换会话密钥RSA=>AES(clientKey)
        context.changeCipher(new AesCipher(clientKey, iv));

        //4.生成可复用session, 用于快速重连
        ReusableSession reusableSession = reusableSessionManager.genSession(context);

        //5.计算心跳时间
        int heartbeat = ConfigTools.getHeartbeat(message.minHeartbeat, message.maxHeartbeat);

        //6.响应握手成功消息
        HandshakeOkMessage
                .from(message)
                .setServerKey(serverKey)
                .setHeartbeat(heartbeat)
                .setSessionId(reusableSession.sessionId)
                .setExpireTime(reusableSession.expireTime)
                .send(f -> {
                    if (f.isSuccess()) {
                        //7.更换会话密钥AES(clientKey)=>AES(sessionKey)
                        context.changeCipher(new AesCipher(sessionKey, iv));
                        //8.保存client信息到当前连接
                        context.setOsName(message.osName)
                                .setOsVersion(message.osVersion)
                                .setClientVersion(message.clientVersion)
                                .setDeviceId(message.deviceId)
                                .setHeartbeat(heartbeat);
                        //9.保存可复用session到Redis, 用于快速重连
                        reusableSessionManager.cacheSession(reusableSession);
                        Logs.CONN.info("handshake success, conn={}", message.getConnection());
                    } else {
                        Logs.CONN.info("handshake failure, conn={}", message.getConnection(), f.cause());
                    }
                });
    }

    private void doInSecurity(HandshakeMessage message) {
        //1.校验客户端消息字段
        if (StringUtil.isBlank(message.deviceId)) {
            ErrorMessage.from(message).setReason("param invalid").close();
            Logs.CONN.error("handshake failure, message={}, conn={}", message, message.getConnection());
            return;
        }

        //2.重复握手判断
        SessionContext context = message.getConnection().getSessionContext();
        if (message.deviceId.equals(context.deviceId)) {
            ErrorMessage.from(message).setErrorCode(ErrorCode.REPEAT_HANDSHAKE).close();
            Logs.CONN.warn("handshake failure, repeat handshake, conn={}", message.getConnection());
            return;
        }

        //3.响应握手成功消息
        HandshakeOkMessage.from(message).send();

        //4.保存client信息到当前连接
        context.setOsName(message.osName)
                .setOsVersion(message.osVersion)
                .setClientVersion(message.clientVersion)
                .setDeviceId(message.deviceId)
                .setHeartbeat(Integer.MAX_VALUE);
        Logs.CONN.info("handshake success, conn={}", message.getConnection());
    }
}
