package com.robust.study.netty.lion.network.http;

import io.netty.handler.codec.http.HttpResponse;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/6 17:29
 * @Version: 1.0
 */
public interface HttpCallback {

    void onResponse(HttpResponse response);

    void onFailure(int statusCode, String reasonPhrase);

    void onException(Throwable throwable);

    void onTimeout();

    boolean onRedirect(HttpResponse response);
}
