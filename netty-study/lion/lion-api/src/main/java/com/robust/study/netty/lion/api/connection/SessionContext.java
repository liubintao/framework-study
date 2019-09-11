package com.robust.study.netty.lion.api.connection;

import com.robust.study.netty.lion.api.router.ClientClassifier;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 17:56
 * @Version: 1.0
 */
public final class SessionContext {
    public String osName;
    public String osVersion;
    public String clientVersion;
    public String deviceId;
    public String userId;
    public String tags;
    public int heartbeat = 100000; //10s
    public Cipher cipher;
    private byte clientType;

    public void changeCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public SessionContext setOsName(String osName) {
        this.osName = osName;
        return this;
    }

    public SessionContext setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public SessionContext setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
        return this;
    }

    public SessionContext setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public SessionContext setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public SessionContext setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
        return this;
    }

    public boolean handshakeOk() {
        return deviceId != null && deviceId.length() > 0;
    }

    public int getClientType() {
        if (clientType == 0) {
            clientType = (byte) ClientClassifier.I.getClientType(osName);
        }
        return clientType;
    }

    public boolean isSecurity() {
        return cipher != null;
    }

    @Override
    public String toString() {

        if (userId == null && deviceId == null) {
            return "";
        }

        return "SessionContext{" +
                "osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", tags='" + tags + '\'' +
                ", heartbeat=" + heartbeat +
                '}';
    }
}
