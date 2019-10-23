
package com.robust.study.netty.lion.client.push;

import com.robust.study.netty.lion.api.push.PushSender;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.client.PusherFactory;

/**
 */
@SPI
public class PushClientFactory implements PusherFactory {
    private volatile PushClient client;

    @Override
    public PushSender get() {
        if (client == null) {
            synchronized (PushClientFactory.class) {
                if (client == null) {
                    client = new PushClient();
                }
            }
        }
        return client;
    }
}
