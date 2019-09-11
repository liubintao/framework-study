package com.robust.study.netty.lion.api.srd;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 17:12
 * @Version: 1.0
 */
public interface ServiceNode {

    String serviceName();

    String nodeId();

    String getHost();

    int getPort();

    default <T> T getAttr(String name) {
        return null;
    }

    default boolean isPersistent() {
        return false;
    }

    default String hostAndPort() {
        return getHost() + ":" + getPort();
    }

    default String nodePath() {
        return serviceName() + '/' + nodeId();
    }
}
