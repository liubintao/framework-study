package com.robust.study.netty.lion.api.srd;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 17:25
 * @Version: 1.0
 */
public final class CommonServiceNode implements ServiceNode {

    private String host;

    private int port;

    private Map<String, Object> attrs;

    private transient String name;

    private transient String nodeId;

    private transient boolean persistent;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServiceName(String name) {
        this.name = name;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public ServiceNode addAttr(String name, Object value) {
        if (attrs == null) {
            attrs = new HashMap<>();
        }
        attrs.put(name, value);
        return this;
    }

    @Override
    public String serviceName() {
        return name;
    }

    @Override
    public String nodeId() {
        return nodeId == null ? UUID.randomUUID().toString() : nodeId;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public <T> T getAttr(String name) {
        return attrs == null || attrs.isEmpty() ? null : (T) attrs.get(name);
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String toString() {
        return "CommonServiceNode{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", attrs=" + attrs +
                ", persistent=" + persistent +
                '}';
    }
}
