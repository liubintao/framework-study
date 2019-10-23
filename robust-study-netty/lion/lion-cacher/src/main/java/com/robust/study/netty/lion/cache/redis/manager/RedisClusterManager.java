package com.robust.study.netty.lion.cache.redis.manager;

import com.robust.study.netty.lion.cache.redis.RedisServer;

import java.util.List;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 14:33
 * @Version: 1.0
 */
public interface RedisClusterManager {
    void init();

    List<RedisServer> getServers();
}
