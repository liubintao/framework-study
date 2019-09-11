package com.robust.study.netty.lion.network.http;

import com.robust.study.netty.lion.api.service.Service;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/9 9:36
 * @Version: 1.0
 */
public interface HttpClient extends Service {
    void request(RequestContext context) throws Exception;
}
