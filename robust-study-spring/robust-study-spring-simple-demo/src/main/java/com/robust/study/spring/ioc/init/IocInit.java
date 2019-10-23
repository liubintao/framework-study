package com.robust.study.spring.ioc.init;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @Description: 演示IOC 容器的初始化包括BeanDefinition 的Resource 定位、载入和注册这三个基本的过程
 * @Author: robust
 * @CreateDate: 2019/10/15 10:20
 * @Version: 1.0
 */
public class IocInit {

    private static void init() {
        // 根据Xml 配置文件创建Resource 资源对象，该对象中包含了BeanDefinition 的信息
        ClassPathResource resource = new ClassPathResource("applicationContext.xml");
        // 创建DefaultListableBeanFactory
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //创建XmlBeanDefinitionReader 读取器，用于载入BeanDefinition。
        // 之所以需要BeanFactory 作为参数，是因为会将读取的信息回调配置给factory
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        // XmlBeanDefinitionReader 执行载入BeanDefinition 的方法，最后会完成Bean 的载入和注册。
        // 完成后Bean 就成功的放置到IOC 容器当中，以后我们就可以从中取得Bean 来使用
        reader.loadBeanDefinitions(resource);
    }

    private static void initByApplication() {
        ApplicationContext context =
                new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
        ((FileSystemXmlApplicationContext) context).refresh();
    }

    public static void main(String[] args) {

        init();
//        initByApplication();
    }
}
