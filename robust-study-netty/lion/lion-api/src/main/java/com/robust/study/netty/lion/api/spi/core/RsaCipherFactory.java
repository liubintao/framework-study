package com.robust.study.netty.lion.api.spi.core;

import com.robust.study.netty.lion.api.connection.Cipher;
import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/4 8:24
 * @Version: 1.0
 */
public interface RsaCipherFactory extends Factory<Cipher> {

    static Cipher create() {
        return SpiLoader.load(RsaCipherFactory.class).get();
    }
}
