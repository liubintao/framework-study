
package com.robust.study.netty.lion.core.push;

import java.util.concurrent.ScheduledExecutorService;


public interface PushTask extends Runnable {
    ScheduledExecutorService getExecutor();
}
