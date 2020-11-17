package com.hankage.hrouter_annotation.enums;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ： 路由的类型  目前只支持activity SERVICE
 */
public enum RouteType {

    /**
     * activity
     */
    ACTIVITY(0, "android.app.Activity"),
    SERVICE(1, "android.app.Service"),
    UNKNOWN(-1, "Unknown route type");

    int id;
    String name;

    RouteType(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RouteType parse(String name){
        for (RouteType routeType : RouteType.values()) {
            if (routeType.name.equals(name)){
                return routeType;
            }
        }

        return UNKNOWN;
    }


}
