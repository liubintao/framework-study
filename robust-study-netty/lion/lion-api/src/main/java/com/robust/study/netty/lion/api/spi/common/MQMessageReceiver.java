package com.robust.study.netty.lion.api.spi.common;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:55
 * @Version: 1.0
 */
public interface MQMessageReceiver {

    void receive(String topic, Object message);
}
