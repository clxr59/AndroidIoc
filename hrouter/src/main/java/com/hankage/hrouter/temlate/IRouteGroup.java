package com.hankage.hrouter.temlate;

import com.hankage.hrouter_annotation.model.RouteMeta;

import java.util.Map;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ：
 */
public interface IRouteGroup {
    /**
     * 加载每个group中的跳转
     */
    Map<String, RouteMeta> loadInto();
}
