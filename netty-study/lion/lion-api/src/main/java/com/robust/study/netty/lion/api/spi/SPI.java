package com.robust.study.netty.lion.api.spi;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:45
 * @Version: 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {
    /**
     * SPI name
     *
     * @return name
     */
    String value() default "";

    /**
     * 排序顺序
     *
     * @return sortNo
     */
    int order() default 0;
}
