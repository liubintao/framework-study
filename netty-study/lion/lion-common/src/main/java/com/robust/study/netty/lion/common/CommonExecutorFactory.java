package com.robust.study.netty.lion.common;

import com.robust.study.netty.lion.api.spi.common.ExecutorFactory;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.log.Logs;
import com.robust.study.netty.lion.tools.thread.NamedPoolThreadFactory;
import com.robust.study.netty.lion.tools.thread.ThreadNames;
import com.robust.study.netty.lion.tools.thread.pool.DefaultExecutor;
import com.robust.study.netty.lion.tools.thread.pool.DumpThreadRejectedHandler;
import com.robust.study.netty.lion.tools.thread.pool.ThreadPoolConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 10:23
 * @Version: 1.0
 */
public class CommonExecutorFactory implements ExecutorFactory {
    @Override
    public Executor get(String name) {
        final ThreadPoolConfig config;
        switch (name) {
            case EVENT_BUS:
                config = ThreadPoolConfig
                        .build(ThreadNames.T_EVENT_BUS)
                        .setCorePoolSize(CC.lion.thread.pool.event_bus.min)
                        .setMaxPoolSize(CC.lion.thread.pool.event_bus.max)
                        .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(10))
                        .setQueueCapacity(CC.lion.thread.pool.event_bus.queue_size)
                        .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
                break;
            case PUSH_CLIENT: {
                ScheduledThreadPoolExecutor executor =
                        new ScheduledThreadPoolExecutor(CC.lion.thread.pool.push_client
                                , new NamedPoolThreadFactory(ThreadNames.T_PUSH_CLIENT_TIMER), (r, e) -> r.run() // run caller thread
                        );
                executor.setRemoveOnCancelPolicy(true);
                return executor;
            }
            case ACK_TIMER: {
                ScheduledThreadPoolExecutor executor =
                        new ScheduledThreadPoolExecutor(CC.lion.thread.pool.ack_timer,
                                new NamedPoolThreadFactory(ThreadNames.T_ARK_REQ_TIMER),
                                (r, e) -> Logs.PUSH.error("one ack context was rejected, context=" + r)
                        );
                executor.setRemoveOnCancelPolicy(true);
                return executor;
            }
            default:
                throw new IllegalArgumentException("no executor for " + name);
        }
        return get(config);
    }

    protected Executor get(ThreadPoolConfig config) {
        String name = config.getName();
        int corePoolSize = config.getCorePoolSize();
        int maxPoolSize = config.getMaxPoolSize();
        int keepAliveSeconds = config.getKeepAliveSeconds();
        BlockingQueue<Runnable> queue = config.getQueue();

        return new DefaultExecutor(corePoolSize
                , maxPoolSize
                , keepAliveSeconds
                , TimeUnit.SECONDS
                , queue
                , new NamedPoolThreadFactory(name)
                , new DumpThreadRejectedHandler(config));
    }
}
