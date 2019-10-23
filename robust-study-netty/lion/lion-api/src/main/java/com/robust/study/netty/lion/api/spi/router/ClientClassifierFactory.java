package com.robust.study.netty.lion.api.spi.router;

import com.robust.study.netty.lion.api.router.ClientClassifier;
import com.robust.study.netty.lion.api.spi.Factory;
import com.robust.study.netty.lion.api.spi.SpiLoader;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 9:25
 * @Version: 1.0
 */
public interface ClientClassifierFactory extends Factory<ClientClassifier> {

    static ClientClassifier create() {
        return SpiLoader.load(ClientClassifierFactory.class).get();
    }
}
