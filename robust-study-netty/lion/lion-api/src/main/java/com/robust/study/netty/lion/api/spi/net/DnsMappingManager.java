
package com.robust.study.netty.lion.api.spi.net;


import com.robust.study.netty.lion.api.service.Service;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 *
 */
public interface DnsMappingManager extends Service {

    static DnsMappingManager create() {
        return SpiLoader.load(DnsMappingManager.class);
    }

    DnsMapping lookup(String origin);
}
