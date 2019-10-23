package com.robust.study.netty.lion.api.spi.core;

import com.robust.study.netty.lion.api.common.ServerEventListener;
import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/10 17:33
 * @Version: 1.0
 */
public interface ServerEventListenerFactory extends Factory<ServerEventListener> {

    static ServerEventListener create() {
        return SpiLoader.load(ServerEventListenerFactory.class).get();
    }
}
