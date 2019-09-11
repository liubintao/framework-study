
package com.robust.study.netty.lion.cache.redis.mq;


import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.MQClient;
import com.robust.study.netty.lion.api.spi.common.MQClientFactory;

/**
 *
 */
@SPI(order = 1)
public final class RedisMQClientFactory implements MQClientFactory {
    private ListenerDispatcher listenerDispatcher = new ListenerDispatcher();

    @Override
    public MQClient get() {
        return listenerDispatcher;
    }
}
