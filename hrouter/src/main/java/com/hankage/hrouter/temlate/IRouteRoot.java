package com.hankage.hrouter.temlate;

import java.util.Map;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ：
 */
public interface IRouteRoot {

    /**
     * 加载所以的Group
     */
    Map<String, Class<? extends IRouteGroup>> loadInfo();
}
