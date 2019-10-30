package com.robust.study.spring.framework.webmvc.servlet.annotation;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/10/22 15:51
 * @Version: 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
