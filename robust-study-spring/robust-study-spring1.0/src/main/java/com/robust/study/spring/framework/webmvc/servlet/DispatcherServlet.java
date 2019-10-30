package com.robust.study.spring.framework.webmvc.servlet;

import com.robust.study.demo.controller.HelloController;
import com.robust.study.spring.framework.webmvc.servlet.annotation.Autowired;
import com.robust.study.spring.framework.webmvc.servlet.annotation.Controller;
import com.robust.study.spring.framework.webmvc.servlet.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/10/22 14:20
 * @Version: 1.0
 */
public class DispatcherServlet extends HttpServlet {

    private Properties context = new Properties();
    private List<String> classNames = new ArrayList<>();
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //开始初始化的步骤

        //1、定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //2、加载
        doScanner(context.getProperty("scanPackage"));
        //3、注册
        doRegistry();
        //4、依赖注入
        doAutowired();
        //5、初始化handlerMapping
        initHandlerMapping();

        HelloController helloController = (HelloController) beanMap.get("helloController");
        helloController.hello(null, null, "zhangsan");
    }

    private void initHandlerMapping() {
    }

    private void doAutowired() {
        if (beanMap.isEmpty()) return;

        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object bean = entry.getValue();

            Field[] fields = bean.getClass().getDeclaredFields();
            Arrays.stream(fields)
                    .filter((f) -> f.isAnnotationPresent(Autowired.class))
                    .forEach((f) -> {
                        Autowired autowired = f.getAnnotation(Autowired.class);
                        String beanName = autowired.value().trim();
                        if ("".equals(beanName)) {
                            beanName = f.getType().getName();
                        }
                        f.setAccessible(true);
                        try {
                            f.set(bean, beanMap.get(beanName));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private void doRegistry() {
        if (classNames.isEmpty()) return;

        classNames.stream().forEach((s -> {
            try {
                Class<?> clazz = Class.forName(s);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    beanMap.put(beanName, clazz.getDeclaredConstructor().newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();
                    if (beanName != null && "".equals(beanName)) {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }

                    Object instance = clazz.newInstance();
                    beanMap.put(beanName, instance);

                    Class<?>[] interfaces = clazz.getInterfaces();
                    Arrays.stream(interfaces).forEach(clz -> {
                        beanMap.put(clz.getName(), instance);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        Arrays.stream(classDir.listFiles()).forEach(file -> {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                classNames.add(scanPackage + "." + file.getName().replace(".class", ""));
            }
        });
    }

    private void doLoadConfig(String location) {
        InputStream resource =
                this.getClass().getClassLoader()
                        .getResourceAsStream(location.replace("classpath:", ""));
        try {
            context.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != resource) {
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String lowerFirstCase(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
