
package com.robust.study.netty.lion.api.spi.common;


import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;
import com.robust.study.netty.lion.api.srd.ServiceDiscovery;

public interface ServiceDiscoveryFactory extends Factory<ServiceDiscovery> {
    static ServiceDiscovery create() {
        return SpiLoader.load(ServiceDiscoveryFactory.class).get();
    }
}
