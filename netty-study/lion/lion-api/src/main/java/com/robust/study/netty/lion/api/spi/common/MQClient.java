package com.robust.study.netty.lion.api.spi.common;

import com.robust.study.netty.lion.api.spi.Plugin;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:53
 * @Version: 1.0
 */
public interface MQClient extends Plugin {

    void subscribe(String topic, MQMessageReceiver receiver);

    void publish(String topic, Object message);
}
