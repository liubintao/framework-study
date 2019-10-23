
package com.robust.study.netty.lion.test.spi;

import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.ServiceRegistryFactory;
import com.robust.study.netty.lion.api.srd.ServiceRegistry;

/**
 */
@SPI(order = 2)
public final class SimpleRegistryFactory implements ServiceRegistryFactory {
    @Override
    public ServiceRegistry get() {
        return FileSrd.I;
    }
}
