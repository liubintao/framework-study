package com.robust.study.netty.lion.core.session;

import com.robust.study.netty.lion.api.connection.SessionContext;
import com.robust.study.netty.lion.common.security.AesCipher;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/5 17:17
 * @Version: 1.0
 */
public final class ReusableSession {
    public String sessionId;
    public long expireTime;
    public SessionContext context;

    public static String encode(SessionContext context) {
        StringBuffer sb = new StringBuffer();
        sb.append(context.osName).append(',');
        sb.append(context.osVersion).append(',');
        sb.append(context.clientVersion).append(',');
        sb.append(context.deviceId).append(',');
        sb.append(context.cipher);
        return sb.toString();
    }

    public static ReusableSession decode(String value) {
        String[] array = value.split(",");
        if (array.length != 6) return null;
        SessionContext context = new SessionContext();
        context.osName = array[0];
        context.osVersion = array[1];
        context.clientVersion = array[2];
        context.deviceId = array[3];
        byte[] key = AesCipher.toArray(array[4]);
        byte[] iv = AesCipher.toArray(array[5]);
        if (key == null || iv == null) return null;
        context.cipher = new AesCipher(key, iv);
        ReusableSession session = new ReusableSession();
        session.context = context;
        return session;
    }
}
