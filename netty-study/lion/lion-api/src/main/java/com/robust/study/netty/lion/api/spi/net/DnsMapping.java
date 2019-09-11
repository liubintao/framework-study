package com.robust.study.netty.lion.api.spi.net;

import java.net.URL;
import java.util.Objects;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 15:49
 * @Version: 1.0
 */
public class DnsMapping {
    protected String ip;
    protected int port;

    public DnsMapping() {
    }

    public DnsMapping(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public DnsMapping setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public DnsMapping setPort(int port) {
        this.port = port;
        return this;
    }

    public static DnsMapping parse(String addr) {
        String[] host_port =
                Objects.requireNonNull(addr, "dns mapping can not be null").split(":");
        if (host_port.length == 1) {
            return new DnsMapping(host_port[0], 80);
        } else {
            return new DnsMapping(host_port[0], Integer.valueOf(host_port[1]));
        }
    }

    public String translate(URL uri) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(uri.getProtocol()).append("//")
                .append(ip)
                .append(':')
                .append(port)
                .append(uri.getPath());
        String query = uri.getQuery();
        if (query != null) {
            sb.append('?').append(query);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DnsMapping that = (DnsMapping) o;
        return port == that.port &&
                Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return "DnsMapping{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
