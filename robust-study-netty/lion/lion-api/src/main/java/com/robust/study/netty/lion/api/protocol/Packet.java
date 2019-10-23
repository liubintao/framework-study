package com.robust.study.netty.lion.api.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @Description: length(4)+cmd(1)+checkCode(2)+flags(1)+sessionId(4)+lrc(1)+body(n)
 * <p>
 * Header       说明
 * 名称           类型      长度      说明
 * length        int       4        表示body 的长度
 * cmd           byte      1        表示消息协议类型
 * checkCode     short     2        是根据body 生成的一个校验码
 * flags         byte      1        表示当前包启用的特性，比如是否启用加密，是否启用压缩
 * sessionId     int       4        消息会话标识用于消息响应
 * lrc           byte      1        纵向冗余校验，用于校验header
 * @Author: robust
 * @CreateDate: 2019/8/28 9:40
 * @Version: 1.0
 */
public class Packet {
    //Header长度
    public static final int HEADER_LENGTH = 13;

    public static final byte FLAG_CRYPTO = 1;//是否加密
    public static final byte FLAG_COMPRESS = 2;//是否压缩
    public static final byte FLAG_BIZ_ACK = 4;
    public static final byte FLAG_AUTO_ACK = 8;
    public static final byte FLAG_JSON_BODY = 16;

    public static final byte HB_PACKET_BYTE = -33;
    public static final byte[] HB_PACKET_BYTES = new byte[]{HB_PACKET_BYTE};
    public static final Packet HB_PACKET = new Packet(Command.HEARTBEAT);

    public byte cmd;//命令
    public transient short cc;//校验码，暂时没有用到
    public byte flags;//特性, 如是否加密，是否压缩等
    public int sessionId;//会话id，客户端生成
    public transient byte lrc;// 校验，纵向冗余校验。只校验head
    public transient byte[] body;

    public Packet(byte cmd) {
        this.cmd = cmd;
    }

    public Packet(byte cmd, int sessionId) {
        this.cmd = cmd;
        this.sessionId = sessionId;
    }

    public Packet(Command command) {
        this.cmd = command.cmd;
    }

    public Packet(Command command, int sessionId) {
        this.cmd = command.cmd;
        this.sessionId = sessionId;
    }

    public int getBodyLength() {
        return body == null ? 0 : body.length;
    }

    public void addFlag(byte flag) {
        this.flags |= flag;
    }

    public boolean hasFlag(byte flag) {
        return (this.flags & flag) != 0;
    }

    public <T> T getBody() {
        return (T) this.body;
    }

    public <T> void setBody(T body) {
        this.body = (byte[]) body;
    }

    public short calcCheckCode() {
        short checkCode = 0;
        if (body != null) {
            for (int i = 0; i < body.length; i++) {
                checkCode += (body[i] & 0x0ff);
            }
        }
        return checkCode;
    }

    public byte calcLrc() {
        byte[] data = Unpooled.buffer(HEADER_LENGTH - 1)
                .writeInt(getBodyLength())
                .writeByte(cmd)
                .writeShort(cc)
                .writeByte(flags)
                .writeInt(sessionId)
                .array();
        byte lrc = 0;
        for (int i = 0; i < data.length; i++) {
            lrc ^= data[i];
        }
        return lrc;
    }

    public boolean validCheckCode() {
        return calcCheckCode() == cc;
    }

    public boolean validLrc() {
        return (calcLrc() ^ lrc) == 0;
    }

    public InetSocketAddress sender() {
        return null;
    }

    public void setRecipient(InetSocketAddress sender) {
    }

    public Packet response(Command command) {
        return new Packet(command, sessionId);
    }

    public Object toFrame(Channel channel) {
        return this;
    }

    public static Packet decodePacket(Packet packet, ByteBuf in, int bodyLength) {
        packet.cc = in.readShort();
        packet.flags = in.readByte();
        packet.sessionId = in.readInt();
        packet.lrc = in.readByte();

        if (bodyLength > 0) {
            in.readBytes(packet.body = new byte[bodyLength]);
        }
        return packet;
    }

    public static void encodePacket(Packet packet, ByteBuf out) {
        if (packet.cmd == Command.HEARTBEAT.cmd) {
            out.writeByte(Packet.HB_PACKET_BYTE);
        } else {
            out.writeInt(packet.getBodyLength());
            out.writeByte(packet.cmd);
            out.writeShort(packet.cc);
            out.writeByte(packet.flags);
            out.writeInt(packet.sessionId);
            out.writeByte(packet.lrc);

            if (packet.getBodyLength() > 0) {
                out.writeBytes(packet.body);
            }
        }
        packet.body = null;
    }

    public static ByteBuf getHBPacket() {
        return Unpooled.wrappedBuffer(HB_PACKET_BYTES);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "cmd=" + cmd +
                ", cc=" + cc +
                ", flags=" + flags +
                ", sessionId=" + sessionId +
                ", lrc=" + lrc +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
