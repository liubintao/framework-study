package com.robust.study.netty.lion.core.server;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.connection.ConnectionManager;
import com.robust.study.netty.lion.network.connection.NettyConnection;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.log.Logs;
import com.robust.study.netty.lion.tools.thread.NamedThreadFactory;
import com.robust.study.netty.lion.tools.thread.ThreadNames;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 9:39
 * @Version: 1.0
 */
public class ServerConnectionManager implements ConnectionManager {

    private final ConcurrentHashMap<ChannelId, ConnectionHolder> connections = new ConcurrentHashMap<>();
    private final ConnectionHolder DEFAULT = new SimpleConnectionHolder(null);
    private final boolean heartbeatCheck;
    private final ConnectionHolderFactory holderFactory;
    private HashedWheelTimer timer;

    public ServerConnectionManager(boolean heartbeatCheck) {
        this.heartbeatCheck = heartbeatCheck;
        this.holderFactory = this.heartbeatCheck ? HeartbeatCheckTask::new : SimpleConnectionHolder::new;
    }

    @Override
    public void init() {
        if (heartbeatCheck) {
            long tickDuration = TimeUnit.SECONDS.toMillis(1);//1s 每秒钟走一步，一个心跳周期内大致走一圈
            int tickPerWheel = (int) (CC.lion.core.max_heartbeat / tickDuration);
            this.timer = new HashedWheelTimer(new NamedThreadFactory(
                    ThreadNames.T_CONN_TIMER), tickDuration, TimeUnit.MILLISECONDS, tickPerWheel);
        }
    }

    @Override
    public void add(Connection connection) {
        this.connections.putIfAbsent(connection.getChannel().id(), holderFactory.create(connection));
    }

    @Override
    public Connection get(Channel channel) {
        return connections.getOrDefault(channel.id(), DEFAULT).get();
    }

    @Override
    public Connection removeAndClose(Channel channel) {
        ConnectionHolder holder = connections.remove(channel.id());
        if (holder != null) {
            Connection conn = holder.get();
            holder.close();
            return conn;
        }

        //add default
        Connection conn = new NettyConnection();
        conn.init(channel, false);
        conn.close();
        return conn;
    }

    @Override
    public int getConnNum() {
        return connections.size();
    }

    @Override
    public void destroy() {
        if (timer != null) {
            timer.stop();
        }
        connections.values().forEach(ConnectionHolder::close);
        connections.clear();
    }

    private interface ConnectionHolder {
        Connection get();

        void close();
    }

    private class SimpleConnectionHolder implements ConnectionHolder {

        private final Connection connection;

        public SimpleConnectionHolder(Connection connection) {
            this.connection = connection;
        }

        @Override
        public Connection get() {
            return this.connection;
        }

        @Override
        public void close() {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @FunctionalInterface
    private interface ConnectionHolderFactory {
        ConnectionHolder create(Connection connection);
    }

    private class HeartbeatCheckTask implements ConnectionHolder, TimerTask {

        private byte timeoutTimes = 0;
        private Connection connection;

        public HeartbeatCheckTask(Connection connection) {
            this.connection = connection;
            this.startTimeout();
        }

        void startTimeout() {
            Connection connection = this.connection;
            if (connection != null && connection.isConnected()) {
                int timeout = connection.getSessionContext().heartbeat;
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
            }
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            Connection conn = this.connection;
            if (conn == null || !conn.isConnected()) {
                Logs.HB.info("heartbeat timeout times={}, connection disconnected, conn={}", timeoutTimes, connection);
                return;
            }

            if (conn.isReadTimeout()) {
                if (++timeoutTimes > CC.lion.core.max_hb_timeout_times) {
                    conn.close();
                    Logs.HB.warn("client heartbeat timeout times={}, do close conn={}", timeoutTimes, connection);
                    return;
                } else {
                    Logs.HB.info("client heartbeat timeout times={}, connection={}", timeoutTimes, connection);
                }
            } else {
                timeoutTimes = 0;
            }

            startTimeout();
        }

        @Override
        public Connection get() {
            return this.connection;
        }

        @Override
        public void close() {
            if (this.connection != null) {
                this.connection.close();
                connection = null;
            }
        }
    }
}
