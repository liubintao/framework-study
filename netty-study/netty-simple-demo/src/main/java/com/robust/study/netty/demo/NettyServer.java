package com.robust.study.netty.demo;

import com.robust.tools.kit.text.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/23 16:51
 * @Version: 1.0
 */
public class NettyServer {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 8080;

    //定义线程数
    private static final int BIZ_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;
    private static final int BIZ_THREAD_SIZE = 100;

    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZ_GROUP_SIZE);
    private static final EventLoopGroup workGroup = new NioEventLoopGroup(BIZ_THREAD_SIZE);

    public static void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = initServerBootstrap();
        ChannelFuture future = serverBootstrap.bind(IP, PORT).sync();
        future.channel().closeFuture().sync();

        System.out.println("server start");
    }

    private static ServerBootstrap initServerBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast(new StringDecoder(Charsets.UTF_8));
                        pipeline.addLast(new StringEncoder(Charsets.UTF_8));
                        pipeline.addLast(new TcpServerHandler());
                    }
                });

        return bootstrap;
    }

    protected void shutDown() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("启动server...");
        NettyServer.start();

    }
}
