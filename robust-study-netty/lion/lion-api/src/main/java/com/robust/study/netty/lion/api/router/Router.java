package com.robust.study.netty.lion.api.router;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 16:08
 * @Version: 1.0
 */
public interface Router<T> {
    T getRouteValue();

    RouterType getRouteType();

    enum RouterType {
        LOCAL, REMOTE
    }
}
