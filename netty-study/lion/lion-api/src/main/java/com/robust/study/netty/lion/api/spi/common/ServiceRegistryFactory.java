
package com.robust.study.netty.lion.api.spi.common;


import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;
import com.robust.study.netty.lion.api.srd.ServiceRegistry;

public interface ServiceRegistryFactory extends Factory<ServiceRegistry> {
    static ServiceRegistry create() {
        return SpiLoader.load(ServiceRegistryFactory.class).get();
    }
}
