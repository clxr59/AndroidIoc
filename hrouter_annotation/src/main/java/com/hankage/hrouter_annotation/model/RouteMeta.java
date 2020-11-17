package com.hankage.hrouter_annotation.model;

import com.hankage.hrouter_annotation.enums.RouteType;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ： 存放路由的相关信息
 */
public class RouteMeta {
    private RouteType routeType;
    private Class<?> destination;
    private String group;
    private String path;

    public RouteMeta() {
    }

    public RouteMeta(String group, String path) {
        this.group = group;
        this.path = path;
    }

    public RouteMeta(RouteType routeType, Class<?> destination, String group, String path) {
        this.routeType = routeType;
        this.destination = destination;
        this.group = group;
        this.path = path;
    }


    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }


    public String getGroup() {
        return group == null ? "" : group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public static RouteMeta build(RouteType routeType, Class<?> destination,  String group, String path){
        return new RouteMeta(routeType, destination, group, path);
    }
}
