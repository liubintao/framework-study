package com.robust.study.netty.lion.api.spi;

import com.robust.study.netty.lion.api.LionContext;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:51
 * @Version: 1.0
 */
public interface Plugin {

    default void init(LionContext context) {
    }

    default void destroy() {
    }
}
