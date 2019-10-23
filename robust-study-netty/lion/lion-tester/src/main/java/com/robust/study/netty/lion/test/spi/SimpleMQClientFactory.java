
package com.robust.study.netty.lion.test.spi;

import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.MQClient;
import com.robust.study.netty.lion.api.spi.common.MQMessageReceiver;

/**
 */
@SPI(order = 2)
public final class SimpleMQClientFactory implements com.robust.study.netty.lion.api.spi.common.MQClientFactory {
    private MQClient mqClient = new MQClient() {
        @Override
        public void subscribe(String topic, MQMessageReceiver receiver) {
            System.err.println("subscribe " + topic);
        }

        @Override
        public void publish(String topic, Object message) {
            System.err.println("publish " + topic + " " + message);
        }
    };

    @Override
    public MQClient get() {
        return mqClient;
    }
}
