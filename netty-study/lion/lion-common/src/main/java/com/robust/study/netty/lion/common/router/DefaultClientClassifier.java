
package com.robust.study.netty.lion.common.router;


import com.robust.study.netty.lion.api.router.ClientClassifier;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.router.ClientClassifierFactory;

/**
 *
 *
 */
@SPI(order = 1)
public final class DefaultClientClassifier implements ClientClassifier, ClientClassifierFactory {

    @Override
    public int getClientType(String osName) {
        return ClientType.find(osName).type;
    }

    @Override
    public ClientClassifier get() {
        return this;
    }
}
