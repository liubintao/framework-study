package com.robust.study.netty.lion.tools.thread.pool;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 10:42
 * @Version: 1.0
 */
public final class DefaultExecutor extends ThreadPoolExecutor {

    public DefaultExecutor(int corePoolSize, int maximumPoolSize,
                           long keepAliveTime, TimeUnit unit,
                           BlockingQueue<Runnable> workQueue,
                           ThreadFactory threadFactory,
                           RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
}
