
package com.robust.study.netty.lion.core;

import com.robust.study.netty.lion.api.push.PushException;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.common.CommonExecutorFactory;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.log.Logs;
import com.robust.study.netty.lion.tools.thread.NamedPoolThreadFactory;
import com.robust.study.netty.lion.tools.thread.ThreadNames;
import com.robust.study.netty.lion.tools.thread.pool.ThreadPoolConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 此线程池可伸缩，线程空闲一定时间后回收，新请求重新创建线程
 */
@SPI(order = 1)
public final class ServerExecutorFactory extends CommonExecutorFactory {

    @Override
    public Executor get(String name) {
        final ThreadPoolConfig config;
        switch (name) {
            case MQ:
                config = ThreadPoolConfig
                        .build(ThreadNames.T_MQ)
                        .setCorePoolSize(CC.lion.thread.pool.mq.min)
                        .setMaxPoolSize(CC.lion.thread.pool.mq.max)
                        .setKeepAliveSeconds(TimeUnit.SECONDS.toSeconds(10))
                        .setQueueCapacity(CC.lion.thread.pool.mq.queue_size)
                        .setRejectedPolicy(ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS);
                break;
            case PUSH_TASK:
                return new ScheduledThreadPoolExecutor(CC.lion.thread.pool.push_task, new NamedPoolThreadFactory(ThreadNames.T_PUSH_CENTER_TIMER),
                        (r, e) -> {
                            throw new PushException("one push task was rejected. task=" + r);
                        }
                );
            case ACK_TIMER: {
                ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(CC.lion.thread.pool.ack_timer,
                        new NamedPoolThreadFactory(ThreadNames.T_ARK_REQ_TIMER),
                        (r, e) -> Logs.PUSH.error("one ack context was rejected, context=" + r)
                );
                executor.setRemoveOnCancelPolicy(true);
                return executor;
            }
            default:
                return super.get(name);
        }

        return get(config);
    }
}
