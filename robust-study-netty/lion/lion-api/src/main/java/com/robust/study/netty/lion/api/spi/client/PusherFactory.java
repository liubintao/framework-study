
package com.robust.study.netty.lion.api.spi.client;


import com.robust.study.netty.lion.api.push.PushSender;
import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

public interface PusherFactory extends Factory<PushSender> {
    static PushSender create() {
        return SpiLoader.load(PusherFactory.class).get();
    }
}
