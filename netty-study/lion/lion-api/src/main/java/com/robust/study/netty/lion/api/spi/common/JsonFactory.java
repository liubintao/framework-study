
package com.robust.study.netty.lion.api.spi.common;

import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;


public interface JsonFactory extends Factory<Json> {

    static Json create() {
        return SpiLoader.load(JsonFactory.class).get();
    }
}
