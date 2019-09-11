package com.robust.study.netty.lion.api.srd;

import com.robust.study.netty.lion.api.service.Service;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 16:42
 * @Version: 1.0
 */
public interface ServiceRegistry extends Service {

    void register(ServiceNode node);

    void deregister(ServiceNode node);
}
