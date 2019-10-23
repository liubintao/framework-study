
package com.robust.study.netty.lion.register.zk;


import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.ServiceRegistryFactory;
import com.robust.study.netty.lion.api.srd.ServiceRegistry;


/**
 */
@SPI(order = 1)
public final class ZKRegistryFactory implements ServiceRegistryFactory {
    @Override
    public ServiceRegistry get() {
        return ZKServiceRegistryAndDiscovery.I;
    }
}
