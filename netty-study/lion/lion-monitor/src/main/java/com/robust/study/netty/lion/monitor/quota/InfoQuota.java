
package com.robust.study.netty.lion.monitor.quota;

public interface InfoQuota extends MonitorQuota {

    String pid();

    double load();
}
