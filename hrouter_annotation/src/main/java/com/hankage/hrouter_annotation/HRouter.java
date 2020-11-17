package com.hankage.hrouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: cheers
 * Time ： 2020/11/12
 * Description ：
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface HRouter {
    // 页面地址
    String path();

    /**
     * 组名
     */
    String group() default "";
}
