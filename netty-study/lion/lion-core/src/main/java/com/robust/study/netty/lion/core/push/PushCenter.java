
package com.robust.study.netty.lion.core.push;

import com.robust.study.netty.lion.api.service.BaseService;
import com.robust.study.netty.lion.api.service.Listener;
import com.robust.study.netty.lion.api.spi.push.IPushMessage;
import com.robust.study.netty.lion.api.spi.push.MessagePusher;
import com.robust.study.netty.lion.api.spi.push.PushListener;
import com.robust.study.netty.lion.api.spi.push.PushListenerFactory;
import com.robust.study.netty.lion.common.qps.FastFlowControl;
import com.robust.study.netty.lion.common.qps.FlowControl;
import com.robust.study.netty.lion.common.qps.GlobalFlowControl;
import com.robust.study.netty.lion.common.qps.RedisFlowControl;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.core.ack.AckTaskQueue;
import com.robust.study.netty.lion.monitor.jmx.MBeanRegistry;
import com.robust.study.netty.lion.monitor.jmx.mxbean.PushCenterBean;
import com.robust.study.netty.lion.tools.config.CC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public final class PushCenter extends BaseService implements MessagePusher {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GlobalFlowControl globalFlowControl = new GlobalFlowControl(
            CC.lion.push.flow_control.global.limit, CC.lion.push.flow_control.global.max, CC.lion.push.flow_control.global.duration
    );

    private final AtomicLong taskNum = new AtomicLong();

    private final AckTaskQueue ackTaskQueue;

    private final LionServer lionServer;

    private PushListener<IPushMessage> pushListener;

    private PushTaskExecutor executor;


    public PushCenter(LionServer lionServer) {
        this.lionServer = lionServer;
        this.ackTaskQueue = new AckTaskQueue(lionServer);
    }

    @Override
    public void push(IPushMessage message) {
        if (message.isBroadcast()) {
            FlowControl flowControl = (message.getTaskId() == null)
                    ? new FastFlowControl(
                    CC.lion.push.flow_control.broadcast.limit,
                    CC.lion.push.flow_control.broadcast.max,
                    CC.lion.push.flow_control.broadcast.duration)
                    : new RedisFlowControl(message.getTaskId(), CC.lion.push.flow_control.broadcast.max);
            addTask(new BroadcastPushTask(lionServer, message, flowControl));
        } else {
            addTask(new SingleUserPushTask(lionServer, message, globalFlowControl));
        }
    }

    public void addTask(PushTask task) {
        executor.addTask(task);
        logger.debug("add new task to push center, count={}, task={}", taskNum.incrementAndGet(), task);
    }

    public void delayTask(long delay, PushTask task) {
        executor.delayTask(delay, task);
        logger.debug("delay task to push center, count={}, task={}", taskNum.incrementAndGet(), task);
    }

    @Override
    protected void doStart(Listener listener) throws Throwable {
        this.pushListener = PushListenerFactory.create();
        this.pushListener.init(lionServer);

        if (CC.lion.net.udpGateway() || CC.lion.thread.pool.push_task > 0) {
            executor = new CustomJDKExecutor(lionServer.getMonitor().getThreadPoolManager().getPushTaskTimer());
        } else {//实际情况使用EventLoo并没有更快，还有待测试
            executor = new NettyEventLoopExecutor();
        }

        MBeanRegistry.getInstance().register(new PushCenterBean(taskNum), null);
        ackTaskQueue.start();
        logger.info("push center start success");
        listener.onSuccess();
    }

    @Override
    protected void doStop(Listener listener) throws Throwable {
        executor.shutdown();
        ackTaskQueue.stop();
        logger.info("push center stop success");
        listener.onSuccess();
    }

    public PushListener<IPushMessage> getPushListener() {
        return pushListener;
    }

    public AckTaskQueue getAckTaskQueue() {
        return ackTaskQueue;
    }

    /**
     * TCP 模式直接使用GatewayServer work 线程池
     */
    private static class NettyEventLoopExecutor implements PushTaskExecutor {

        @Override
        public void shutdown() {
        }

        @Override
        public void addTask(PushTask task) {
            task.getExecutor().execute(task);
        }

        @Override
        public void delayTask(long delay, PushTask task) {
            task.getExecutor().schedule(task, delay, TimeUnit.NANOSECONDS);
        }
    }


    /**
     * UDP 模式使用自定义线程池
     */
    private static class CustomJDKExecutor implements PushTaskExecutor {
        private final ScheduledExecutorService executorService;

        private CustomJDKExecutor(ScheduledExecutorService executorService) {
            this.executorService = executorService;
        }

        @Override
        public void shutdown() {
            executorService.shutdown();
        }

        @Override
        public void addTask(PushTask task) {
            executorService.execute(task);
        }

        @Override
        public void delayTask(long delay, PushTask task) {
            executorService.schedule(task, delay, TimeUnit.NANOSECONDS);
        }
    }

    private interface PushTaskExecutor {

        void shutdown();

        void addTask(PushTask task);

        void delayTask(long delay, PushTask task);
    }
}
