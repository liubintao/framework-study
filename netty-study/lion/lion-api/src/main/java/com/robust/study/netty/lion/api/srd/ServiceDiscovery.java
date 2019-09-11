package com.robust.study.netty.lion.api.srd;

import com.robust.study.netty.lion.api.service.Service;

import java.util.List;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:39
 * @Version: 1.0
 */
public interface ServiceDiscovery extends Service {

    List<ServiceNode> lookup(String path);

    void subscribe(String path, ServiceListener listener);

    void unsubscribe(String path, ServiceListener listener);
}
