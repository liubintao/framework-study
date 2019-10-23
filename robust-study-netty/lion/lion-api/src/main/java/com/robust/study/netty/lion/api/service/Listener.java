package com.robust.study.netty.lion.api.service;

/**
 * @Description: 监听器, 当任务成功/失败时触发
 * @Author: robust
 * @CreateDate: 2019/8/27 15:45
 * @Version: 1.0
 */
public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable throwable);
}
