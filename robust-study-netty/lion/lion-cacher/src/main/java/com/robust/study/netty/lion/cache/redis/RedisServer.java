package com.robust.study.netty.lion.cache.redis;

import com.robust.study.netty.lion.tools.config.data.RedisNode;
import redis.clients.jedis.HostAndPort;

/**
 * redis 相关的配置信息
 */
public class RedisServer extends RedisNode {

    public RedisServer(String host, int port) {
        super(host, port);
    }

    public HostAndPort convert() {
        return new HostAndPort(host, port);
    }
}
