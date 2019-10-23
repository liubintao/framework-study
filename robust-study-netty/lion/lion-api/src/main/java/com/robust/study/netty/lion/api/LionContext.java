package com.robust.study.netty.lion.api;

import com.robust.study.netty.lion.api.common.Monitor;
import com.robust.study.netty.lion.api.spi.common.CacheManager;
import com.robust.study.netty.lion.api.spi.common.MQClient;
import com.robust.study.netty.lion.api.srd.ServiceDiscovery;
import com.robust.study.netty.lion.api.srd.ServiceRegistry;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 15:35
 * @Version: 1.0
 */
public interface LionContext {

    Monitor getMonitor();

    ServiceDiscovery getDiscovery();

    ServiceRegistry getRegistry();

    CacheManager getCacheManager();

    MQClient getMQClient();
}
