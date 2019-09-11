package com.robust.study.netty.lion.api.spi.handler;

import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 14:39
 * @Version: 1.0
 */
public interface BindValidatorFactory extends Factory<BindValidator> {

    static BindValidator create() {
        return SpiLoader.load(BindValidatorFactory.class).get();
    }
}
