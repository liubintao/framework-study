package com.robust.study.netty.lion.core.server;

import com.robust.study.netty.lion.api.connection.ConnectionManager;
import com.robust.study.netty.lion.api.protocol.Command;
import com.robust.study.netty.lion.api.service.Listener;
import com.robust.study.netty.lion.api.spi.handler.PushHandlerFactory;
import com.robust.study.netty.lion.common.MessageDispatcher;
import com.robust.study.netty.lion.core.LionServer;
import com.robust.study.netty.lion.core.handler.*;
import com.robust.study.netty.lion.network.server.NettyTcpServer;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.thread.NamedThreadFactory;
import com.robust.study.netty.lion.tools.thread.ThreadNames;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/3 16:26
 * @Version: 1.0
 */
public final class ConnectionServer extends NettyTcpServer {

    private ServerChannelHandler channelHandler;
    private GlobalChannelTrafficShapingHandler trafficShapingHandler;
    private ScheduledExecutorService trafficShapingExecutor;
    private MessageDispatcher messageDispatcher;
    private ConnectionManager connectionManager;
    private LionServer lionServer;

    public ConnectionServer(LionServer lionServer) {
        super(CC.lion.net.connect_server_port, CC.lion.net.connect_server_bind_ip);
        this.lionServer = lionServer;
        this.connectionManager = new ServerConnectionManager(true);
        this.messageDispatcher = new MessageDispatcher();
        this.channelHandler = new ServerChannelHandler(true, this.connectionManager, this.messageDispatcher);
    }

    @Override
    public void init() {
        //调用父类初始化, 设置状态为初始化中
        super.init();
        //初始化连接管理器的心跳检测
        connectionManager.init();
        //注册处理器
        messageDispatcher.register(Command.HEARTBEAT, HeartBeatHandler::new);
        messageDispatcher.register(Command.HANDSHAKE, new HandshakeHandler(lionServer));
        messageDispatcher.register(Command.BIND, new BindUserHandler(lionServer));
        messageDispatcher.register(Command.UNBIND, new BindUserHandler(lionServer));
        messageDispatcher.register(Command.FAST_CONNECT, new FastConnectHandler(lionServer));
        messageDispatcher.register(Command.PUSH, PushHandlerFactory::create);
        messageDispatcher.register(Command.ACK, new AckHandler(lionServer));
        messageDispatcher.register(Command.HTTP_PROXY, new HttpProxyHandler(lionServer));

        //启用流量整形, 限流
        if (CC.lion.net.traffic_shaping.connect_server.enabled) {
            trafficShapingExecutor = Executors.newSingleThreadScheduledExecutor(
                    new NamedThreadFactory(ThreadNames.T_TRAFFIC_SHAPING));
            trafficShapingHandler = new GlobalChannelTrafficShapingHandler(
                    trafficShapingExecutor,
                    CC.lion.net.traffic_shaping.connect_server.write_global_limit,
                    CC.lion.net.traffic_shaping.connect_server.read_global_limit,
                    CC.lion.net.traffic_shaping.connect_server.write_channel_limit,
                    CC.lion.net.traffic_shaping.connect_server.read_channel_limit,
                    CC.lion.net.traffic_shaping.connect_server.check_interval
            );
        }
    }

    @Override
    public void start(Listener listener) {
        super.start(listener);
        //增加线程池监控
        if (this.workerGroup != null) {
            lionServer.getMonitor().monitor("conn-worker", workerGroup);
        }
    }

    @Override
    public void stop(Listener listener) {
        super.stop(listener);
        if (trafficShapingHandler != null) {
            trafficShapingHandler.release();
            trafficShapingExecutor.shutdown();
        }
        connectionManager.destroy();
    }

    @Override
    protected ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    @Override
    protected int getWorkThreadNum() {
        return CC.lion.thread.pool.conn_work;
    }

    @Override
    protected String getBossThreadName() {
        return ThreadNames.T_CONN_BOSS;
    }

    @Override
    protected String getWorkThreadName() {
        return ThreadNames.T_CONN_WORKER;
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        super.initPipeline(pipeline);
        if (trafficShapingHandler != null) {
            pipeline.addFirst(trafficShapingHandler);
        }
    }

    @Override
    protected void initOptions(ServerBootstrap bootstrap) {
        super.initOptions(bootstrap);

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

        /**
         * TCP层面的接收和发送缓冲区大小设置，
         * 在Netty中分别对应ChannelOption的SO_SNDBUF和SO_RCVBUF，
         * 需要根据推送消息的大小，合理设置，对于海量长连接，通常32K是个不错的选择。
         */
        if (CC.lion.net.snd_buf.connect_server > 0) {
            bootstrap.option(ChannelOption.SO_SNDBUF, CC.lion.net.snd_buf.connect_server);
        }
        if (CC.lion.net.rcv_buf.connect_server > 0) {
            bootstrap.option(ChannelOption.SO_RCVBUF, CC.lion.net.rcv_buf.connect_server);
        }

        /**
         * 这个坑其实也不算坑，只是因为懒，该做的事情没做。一般来讲我们的业务如果比较小的时候我们用同步处理，等业务到一定规模的时候，一个优化手段就是异步化。
         * 异步化是提高吞吐量的一个很好的手段。但是，与异步相比，同步有天然的负反馈机制，也就是如果后端慢了，前面也会跟着慢起来，可以自动的调节。
         * 但是异步就不同了，异步就像决堤的大坝一样，洪水是畅通无阻。如果这个时候没有进行有效的限流措施就很容易把后端冲垮。
         * 如果一下子把后端冲垮倒也不是最坏的情况，就怕把后端冲的要死不活。
         * 这个时候，后端就会变得特别缓慢，如果这个时候前面的应用使用了一些无界的资源等，就有可能把自己弄死。
         * 那么现在要介绍的这个坑就是关于Netty里的ChannelOutboundBuffer这个东西的。
         * 这个buffer是用在netty向channel write数据的时候，有个buffer缓冲，这样可以提高网络的吞吐量(每个channel有一个这样的buffer)。
         * 初始大小是32(32个元素，不是指字节)，但是如果超过32就会翻倍，一直增长。
         * 大部分时候是没有什么问题的，但是在碰到对端非常慢(对端慢指的是对端处理TCP包的速度变慢，比如对端负载特别高的时候就有可能是这个情况)的时候就有问题了，
         * 这个时候如果还是不断地写数据，这个buffer就会不断地增长，最后就有可能出问题了(我们的情况是开始吃swap，最后进程被linux killer干掉了)。
         * 为什么说这个地方是坑呢，因为大部分时候我们往一个channel写数据会判断channel是否active，但是往往忽略了这种慢的情况。
         *
         * 那这个问题怎么解决呢？其实ChannelOutboundBuffer虽然无界，但是可以给它配置一个高水位线和低水位线，
         * 当buffer的大小超过高水位线的时候对应channel的isWritable就会变成false，
         * 当buffer的大小低于低水位线的时候，isWritable就会变成true。所以应用应该判断isWritable，如果是false就不要再写数据了。
         * 高水位线和低水位线是字节数，默认高水位是64K，低水位是32K，我们可以根据我们的应用需要支持多少连接数和系统资源进行合理规划。
         */
        bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(
                CC.lion.net.write_buffer_water_mark.connect_server_low,
                CC.lion.net.write_buffer_water_mark.connect_server_high));
    }

    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    public MessageDispatcher getMessageDispatcher() {
        return this.messageDispatcher;
    }
}
