package com.robust.study.netty.lion.tools.thread.pool;

import com.robust.study.netty.lion.tools.Utils;
import com.robust.study.netty.lion.tools.common.JVMUtil;
import com.robust.study.netty.lion.tools.config.CC;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 10:43
 * @Version: 1.0
 */
@Slf4j
public final class DumpThreadRejectedHandler implements RejectedExecutionHandler {

    private volatile boolean dumping = false;

    private static final String DUMP_DIR = CC.lion.monitor.dump_dir;

    private final ThreadPoolConfig poolConfig;

    private final int rejectedPolicy;

    public DumpThreadRejectedHandler(ThreadPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        this.rejectedPolicy = poolConfig.getRejectedPolicy();
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.warn("one task rejected, poolConfig={}, poolInfo={}", poolConfig, Utils.getPoolInfo(executor));
        if (!dumping) {
            dumping = true;
            dumpJVMInfo();
        }

        if (rejectedPolicy == ThreadPoolConfig.REJECTED_POLICY_ABORT) {
            throw new RejectedExecutionException("one task rejected, pool=" + poolConfig.getName());
        } else if (rejectedPolicy == ThreadPoolConfig.REJECTED_POLICY_CALLER_RUNS) {
            if (!executor.isShutdown()) {
                r.run();
            }
        }
    }

    private void dumpJVMInfo() {
        log.info("start dump jvm info");
        JVMUtil.dumpJstack(DUMP_DIR + "/" + poolConfig.getName());
        log.info("end dump jvm info");
    }
}
