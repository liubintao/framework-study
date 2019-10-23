package com.robust.study.netty.lion.api.spi.handler;

import com.robust.study.netty.lion.api.spi.Plugin;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 14:38
 * @Version: 1.0
 */
public interface BindValidator extends Plugin {

    boolean validate(String userId, String data);
}
