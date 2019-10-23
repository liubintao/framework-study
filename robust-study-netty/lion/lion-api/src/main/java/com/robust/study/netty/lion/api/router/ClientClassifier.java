package com.robust.study.netty.lion.api.router;

import com.robust.study.netty.lion.api.spi.router.ClientClassifierFactory;

/**
 * @Description: 客户端分类
 * @Author: robust
 * @CreateDate: 2019/8/28 9:21
 * @Version: 1.0
 */
public interface ClientClassifier {

    ClientClassifier I = ClientClassifierFactory.create();

    int getClientType(String osName);
}
