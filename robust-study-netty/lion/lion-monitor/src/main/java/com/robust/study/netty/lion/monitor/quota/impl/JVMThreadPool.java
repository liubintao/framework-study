
package com.robust.study.netty.lion.monitor.quota.impl;

import com.robust.study.netty.lion.monitor.quota.ThreadPoolQuota;
import com.robust.study.netty.lion.monitor.service.ThreadPoolManager;
import io.netty.channel.EventLoopGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import static com.robust.study.netty.lion.tools.Utils.getPoolInfo;


public class JVMThreadPool implements ThreadPoolQuota {
    private final ThreadPoolManager threadPoolManager;

    public JVMThreadPool(ThreadPoolManager threadPoolManager) {
        this.threadPoolManager = threadPoolManager;
    }

    @Override
    public Object monitor(Object... args) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Executor> pools = threadPoolManager.getActivePools();
        for (Map.Entry<String, Executor> entry : pools.entrySet()) {
            String serviceName = entry.getKey();
            Executor executor = entry.getValue();
            if (executor instanceof ThreadPoolExecutor) {
                result.put(serviceName, getPoolInfo((ThreadPoolExecutor) executor));
            } else if (executor instanceof EventLoopGroup) {
                result.put(serviceName, getPoolInfo((EventLoopGroup) executor));
            }
        }
        return result;
    }
}
