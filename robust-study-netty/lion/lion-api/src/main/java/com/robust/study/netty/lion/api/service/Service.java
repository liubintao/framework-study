package com.robust.study.netty.lion.api.service;

import java.util.concurrent.CompletableFuture;

/**
 * @Description: 基础服务接口
 * @Author: robust
 * @CreateDate: 2019/8/27 15:46
 * @Version: 1.0
 */
public interface Service {

    void start(Listener listener);

    void stop(Listener listener);

    CompletableFuture<Boolean> start();

    CompletableFuture<Boolean> stop();

    boolean syncStart();

    boolean syncStop();

    void init();

    boolean isRunning();
}
