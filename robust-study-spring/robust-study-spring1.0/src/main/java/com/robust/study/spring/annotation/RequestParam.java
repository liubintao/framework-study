package com.robust.study.spring.annotation;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/10/22 15:16
 * @Version: 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
