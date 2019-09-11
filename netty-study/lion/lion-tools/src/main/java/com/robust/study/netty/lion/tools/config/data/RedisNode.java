package com.robust.study.netty.lion.tools.config.data;

import java.util.Objects;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 16:03
 * @Version: 1.0
 */
public class RedisNode {
    public String host;
    public int port;

    public RedisNode() {
    }

    public RedisNode(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static RedisNode from(String config) {
        String[] array = config.split(":");
        if (array.length == 1) {
            return new RedisNode(array[0], 80);
        } else {
            return new RedisNode(array[0], Integer.valueOf(array[1]));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisNode redisNode = (RedisNode) o;
        return port == redisNode.port &&
                Objects.equals(host, redisNode.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return "RedisNode{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
