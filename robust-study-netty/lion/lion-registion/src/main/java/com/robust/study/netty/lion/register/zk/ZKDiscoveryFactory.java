
package com.robust.study.netty.lion.register.zk;


import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.common.ServiceDiscoveryFactory;
import com.robust.study.netty.lion.api.srd.ServiceDiscovery;

/**
 *
 */
@SPI(order = 1)
public final class ZKDiscoveryFactory implements ServiceDiscoveryFactory {
    @Override
    public ServiceDiscovery get() {
        return ZKServiceRegistryAndDiscovery.I;
    }
}
