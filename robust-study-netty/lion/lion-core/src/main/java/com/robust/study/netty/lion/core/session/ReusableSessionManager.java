package com.robust.study.netty.lion.core.session;

import com.robust.study.netty.lion.api.connection.SessionContext;
import com.robust.study.netty.lion.api.spi.common.CacheManager;
import com.robust.study.netty.lion.api.spi.common.CacheManagerFactory;
import com.robust.study.netty.lion.common.CacheKeys;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.crypto.MD5Utils;
import com.robust.tools.kit.text.StringUtil;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/5 16:57
 * @Version: 1.0
 */
public final class ReusableSessionManager {
    private final int expiredTime = CC.lion.core.session_expired_time;
    private final CacheManager cacheManager = CacheManagerFactory.create();

    public boolean cacheSession(ReusableSession session) {
        String key = CacheKeys.getSessionKey(session.sessionId);
        cacheManager.set(key, ReusableSession.encode(session.context), expiredTime);
        return true;
    }

    public ReusableSession querySession(String sessionId) {
        String key = CacheKeys.getSessionKey(sessionId);
        String value = cacheManager.get(key, String.class);
        return StringUtil.isBlank(value) ? null : ReusableSession.decode(value);
    }

    public ReusableSession genSession(SessionContext context) {
        long now = System.currentTimeMillis();
        ReusableSession session = new ReusableSession();
        session.context = context;
        session.sessionId = MD5Utils.encrypt(context.deviceId + now);
        session.expireTime = now + expiredTime * 1000;
        return session;
    }
}
