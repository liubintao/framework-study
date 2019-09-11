package com.robust.study.netty.lion.api.spi.handler;

import com.robust.study.netty.lion.api.message.MessageHandler;
import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 16:45
 * @Version: 1.0
 */
public interface PushHandlerFactory extends Factory<MessageHandler> {

    static MessageHandler create() {
        return SpiLoader.load(PushHandlerFactory.class).get();
    }
}
