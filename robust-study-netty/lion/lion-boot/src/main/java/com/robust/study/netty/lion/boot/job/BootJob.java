package com.robust.study.netty.lion.boot.job;

import com.robust.study.netty.lion.tools.log.Logs;

import java.util.function.Supplier;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/10 17:22
 * @Version: 1.0
 */
public abstract class BootJob {
    protected BootJob next;

    protected abstract void start();

    protected abstract void stop();

    public void startNext() {
        if (next != null) {
            Logs.Console.info("start bootstrap job [{}]", getNextName());
            next.start();
        }
    }

    public void stopNext() {
        next.stop();
        Logs.Console.info("stopped bootstrap job [{}]", getNextName());
    }

    public BootJob setNext(BootJob next) {
        this.next = next;
        return next;
    }

    public BootJob setNext(Supplier<BootJob> next, boolean enabled) {
        if (enabled) {
            return setNext(next.get());
        }
        return this;
    }

    protected String getNextName() {
        return next.getName();
    }

    protected String getName() {
        return this.getClass().getSimpleName();
    }
}
