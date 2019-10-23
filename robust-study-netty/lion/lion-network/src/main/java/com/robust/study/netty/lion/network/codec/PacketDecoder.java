package com.robust.study.netty.lion.network.codec;

import com.robust.study.netty.lion.api.protocol.JsonPacket;
import com.robust.study.netty.lion.api.protocol.Packet;
import com.robust.study.netty.lion.api.protocol.UDPPacket;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.tools.kit.mapper.JsonMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @Description: length(4)+cmd(1)+cc(2)+flags(1)+sessionId(4)+lrc(1)+body(n)
 * @Author: robust
 * @CreateDate: 2019/9/3 9:33
 * @Version: 1.0
 */
public final class PacketDecoder extends ByteToMessageDecoder {

    private static final int maxPacketSize = CC.lion.core.max_packet_size;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        decodeHeartbeat(in, out);
        decodeFrames(in, out);
    }

    private void decodeHeartbeat(ByteBuf in, List<Object> out) {
        while (in.isReadable()) {
            if (in.readByte() == Packet.HB_PACKET_BYTE) {
                out.add(Packet.HB_PACKET);
            } else {
                in.readerIndex(in.readerIndex() - 1);
                break;
            }
        }
    }

    private void decodeFrames(ByteBuf in, List<Object> out) {
        if (in.readableBytes() > Packet.HEADER_LENGTH) {
            //1.记录当前读取位置.如果读取到非完整的frame,要恢复到该位置,便于下次读取
            in.markReaderIndex();

            Packet packet = decodeFrame(in);
            if (packet != null) {
                out.add(packet);
            } else {
                //2.读取到不完整的frame,恢复到最近一次正常读取的位置,便于下次读取
                in.resetReaderIndex();
            }
        }
    }

    private Packet decodeFrame(ByteBuf in) {
        int readableBytes = in.readableBytes();
        int bodyLength = in.readInt();

        if (readableBytes < (bodyLength + Packet.HEADER_LENGTH)) {
            return null;
        }

        if (bodyLength > maxPacketSize) {
            throw new TooLongFrameException("packet body length over limit:" + bodyLength);
        }

        return Packet.decodePacket(new Packet(in.readByte()), in, bodyLength);
    }

    public static Packet decodeFrame(DatagramPacket frame) {
        ByteBuf in = frame.content();
        int readableBytes = in.readableBytes();
        int bodyLength = in.readInt();

        if (readableBytes < (bodyLength + Packet.HEADER_LENGTH)) {
            return null;
        }

        return Packet.decodePacket(new UDPPacket(in.readByte()), in, bodyLength);
    }

    public static Packet decodeFrame(String frame) {
        if (frame == null) {
            return null;
        }
        return JsonMapper.INSTANCE.fromJson(frame, JsonPacket.class);
    }
}
