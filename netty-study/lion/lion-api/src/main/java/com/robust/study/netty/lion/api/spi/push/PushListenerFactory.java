

package com.robust.study.netty.lion.api.spi.push;


import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 *
 *
 */
public interface PushListenerFactory<M extends IPushMessage> extends Factory<PushListener<M>> {

    @SuppressWarnings("unchecked")
    static <M extends IPushMessage> PushListener<M> create() {
        return (PushListener<M>) SpiLoader.load(PushListenerFactory.class).get();
    }
}
