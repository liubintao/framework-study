package com.robust.study.netty.lion.network.udp;

import com.robust.study.netty.lion.api.connection.Connection;
import com.robust.study.netty.lion.api.message.PacketReceiver;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.network.codec.PacketDecoder;
import com.robust.study.netty.lion.network.connection.NettyConnection;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 11:57
 * @Version: 1.0
 */
@ChannelHandler.Sharable
@Slf4j
public final class UDPChannelHandler extends ChannelInboundHandlerAdapter {

    private final NettyConnection connection = new NettyConnection();
    private final PacketReceiver receiver;
    private InetAddress multicastAddress;
    private NetworkInterface networkInterface;

    public UDPChannelHandler(PacketReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connection.init(ctx.channel(), false);
        if (multicastAddress != null) {
            ((DatagramChannel) ctx.channel()).joinGroup(multicastAddress, networkInterface, null)
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            log.info("join multicast group success, channel={}, group={}", ctx.channel(), multicastAddress);
                        } else {
                            log.error("join multicast group error, channel={}, group={}", ctx.channel(), multicastAddress, future.cause());
                        }
                    });
            log.info("init udp channel={}", ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connection.close();
        if (multicastAddress != null) {
            ((DatagramChannel) ctx.channel()).leaveGroup(multicastAddress, networkInterface, null).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("leave multicast group success, channel={}, group={}", ctx.channel(), multicastAddress);
                } else {
                    log.error("leave multicast group error, channel={}, group={}", ctx.channel(), multicastAddress, future.cause());
                }
            });
        }
        log.info("disconnect udp channel={}, connection={}", ctx.channel(), connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket datagramPacket = (DatagramPacket) msg;
        Packet packet = PacketDecoder.decodeFrame(datagramPacket);
        receiver.onReceive(packet, connection);
        datagramPacket.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        connection.close();
        log.error("udp handler caught an exception, channel={}, conn={}", ctx.channel(), connection, cause);
    }

    public UDPChannelHandler setMulticastAddress(InetAddress multicastAddress) {
        if (!multicastAddress.isMulticastAddress()) {
            throw new IllegalArgumentException(multicastAddress + "not a multicastAddress");
        }

        this.multicastAddress = multicastAddress;
        return this;
    }

    public UDPChannelHandler setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
        return this;
    }

    public Connection getConnection() {
        return connection;
    }
}
