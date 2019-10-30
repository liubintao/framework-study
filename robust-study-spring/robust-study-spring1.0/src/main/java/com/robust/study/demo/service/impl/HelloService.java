package com.robust.study.demo.service.impl;

import com.robust.study.demo.service.IHelloService;
import com.robust.study.spring.framework.webmvc.servlet.annotation.Service;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/10/22 15:50
 * @Version: 1.0
 */
@Service
public class HelloService implements IHelloService {
    @Override
    public String get(String name) {
        return "hi: " + name;
    }
}
