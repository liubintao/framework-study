package com.robust.study.netty.lion.api.event;

import com.robust.study.netty.lion.api.router.Router;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 15:49
 * @Version: 1.0
 */
public final class RouterChangeEvent implements Event {
    public final String userId;
    public final Router<?> router;

    public RouterChangeEvent(String userId, Router<?> router) {
        this.userId = userId;
        this.router = router;
    }
}
