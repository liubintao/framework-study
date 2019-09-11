package com.robust.study.netty.lion.api.spi;

import java.util.function.Supplier;

/**
 * @Description: SPI生产工厂接口
 * @Author: robust
 * @CreateDate: 2019/8/28 9:24
 * @Version: 1.0
 */
@FunctionalInterface
public interface Factory<T> extends Supplier<T> {
}
