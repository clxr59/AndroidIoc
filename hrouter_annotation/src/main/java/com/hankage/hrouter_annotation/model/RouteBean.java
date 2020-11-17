package com.hankage.hrouter_annotation.model;

import com.hankage.hrouter_annotation.enums.RouteType;

import javax.lang.model.element.Element;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ： 存放路由的相关信息
 */
public class RouteBean {
    private RouteType routeType;
    private Class<?> destination;
    private Element element;
    private String group;
    private String path;

    public RouteBean(RouteType routeType, Class<?> destination, Element element, String group, String path) {
        this.routeType = routeType;
        this.destination = destination;
        this.element = element;
        this.group = group;
        this.path = path;
    }

    public RouteBean(RouteType routeType, Class<?> destination, String group, String path) {
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

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
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


    public static RouteBean build(RouteType routeType, Element element, String group, String path){
        return new RouteBean(routeType, null, element, group, path);
    }

    public static RouteBean build(RouteType routeType, Class<?> destination, String group, String path){
        return new RouteBean(routeType, destination, group, path);
    }
}
