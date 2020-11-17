package com.hankage.hrouter_compiler;

/**
 * Author: cheers
 * Time ： 2020/11/12
 * Description ：
 */
public interface ProcessorConstant {
    String MODULE_NAME = "MODULE_NAME";

    String ACTIVITY = "android.app.Activity";

    String ROUTER_PACKAGE_PATH = "com.hankage.hrouter.temlate";

    String ROUTER_GROUP = ROUTER_PACKAGE_PATH + ".IRouteGroup";

    String ROUTER_ROOT = ROUTER_PACKAGE_PATH + ".IRouteRoot";


    String ROUTER_GROUP_METHOD_NAME = "loadInto";

    String ROUTER_ROOT_METHOD_NAME = "loadInfo";

    String ROUTER_GROUP_CLASS_NAME = "HRouter$$Group$$";

    String ROUTER_ROOT_CLASS_NAME = "HRouter$$Root$$";

    String ROUTER_ROOT_PACKAGE_NAME = "com.hankage.hrouter";

}
