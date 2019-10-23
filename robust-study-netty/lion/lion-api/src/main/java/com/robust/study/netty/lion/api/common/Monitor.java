package com.robust.study.netty.lion.api.common;

import java.util.concurrent.Executor;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 15:39
 * @Version: 1.0
 */
public interface Monitor {

    void monitor(String name, Thread thread);

    void monitor(String name, Executor executor);
}
