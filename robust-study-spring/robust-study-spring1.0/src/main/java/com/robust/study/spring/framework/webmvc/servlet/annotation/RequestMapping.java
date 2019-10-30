package com.robust.study.spring.framework.webmvc.servlet.annotation;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/10/22 15:16
 * @Version: 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
