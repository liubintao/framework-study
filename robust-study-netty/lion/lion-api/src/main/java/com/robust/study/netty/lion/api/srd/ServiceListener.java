package com.robust.study.netty.lion.api.srd;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 17:19
 * @Version: 1.0
 */
public interface ServiceListener {

    void onServiceAdded(String path, ServiceNode node);

    void onServiceUpdated(String path, ServiceNode node);

    void onServiceRemoved(String path, ServiceNode node);
}
