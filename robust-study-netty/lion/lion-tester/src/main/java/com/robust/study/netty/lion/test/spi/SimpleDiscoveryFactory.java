
package com.robust.study.netty.lion.test.spi;

import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.ServiceDiscoveryFactory;
import com.robust.study.netty.lion.api.srd.ServiceDiscovery;

/**
 */
@SPI(order = 2)
public final class SimpleDiscoveryFactory implements ServiceDiscoveryFactory {
    @Override
    public ServiceDiscovery get() {
        return FileSrd.I;
    }
}
