package com.hankage.viewbing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author: cheers
 * Time ： 2020/11/7
 * Description ：
 * @author hankage
 */
public class EventInvocationHandler implements InvocationHandler {

    private Object proxyObject;
    private Method proxyMethod;

    public EventInvocationHandler(Object object, Method method) {
        this.proxyObject = object;
        this.proxyMethod = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return proxyMethod.invoke(proxyObject, args);
    }
}
