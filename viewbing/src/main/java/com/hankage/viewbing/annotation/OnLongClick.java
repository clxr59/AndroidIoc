package com.hankage.viewbing.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: cheers
 * Time ： 2020/11/7
 * Description ：
 * @author hankage
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// 这里使用的是注解的范型
@BaseEvent(listenerName = "setOnLongClickListener",
        listenerClazz = View.OnLongClickListener.class)
public @interface OnLongClick {
    int[] value();
}
