package com.robust.study.netty.lion.api.event;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 20:03
 * @Version: 1.0
 */
public class KickUserEvent implements Event {
    public final String userId;
    public final String deviceId;
    public final String fromServer;

    public KickUserEvent(String userId, String deviceId, String fromServer) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.fromServer = fromServer;
    }
}
