package com.robust.study.demo.controller;

import com.robust.study.demo.service.IHelloService;
import com.robust.study.spring.framework.webmvc.servlet.annotation.Autowired;
import com.robust.study.spring.framework.webmvc.servlet.annotation.Controller;
import com.robust.study.spring.framework.webmvc.servlet.annotation.RequestMapping;
import com.robust.study.spring.framework.webmvc.servlet.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/10/22 15:45
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/hello")
public class HelloController {

    @Autowired
    private IHelloService helloService;

    @RequestMapping(value = "name")
    public void hello(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("name") String name) {
        String ret = helloService.get(name);
        System.out.println(ret);
    }
}
