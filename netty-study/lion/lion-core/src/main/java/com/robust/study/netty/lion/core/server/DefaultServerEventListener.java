
package com.robust.study.netty.lion.core.server;


import com.robust.study.netty.lion.api.common.ServerEventListener;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.core.ServerEventListenerFactory;

@SPI(order = 1)
public final class DefaultServerEventListener implements ServerEventListener, ServerEventListenerFactory {

    @Override
    public ServerEventListener get() {
        return this;
    }
}
