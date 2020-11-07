package com.hankage.viewbing;

import com.hankage.viewbing.annotation.BaseEvent;
import com.hankage.viewbing.annotation.BindView;
import com.hankage.viewbing.annotation.ContentView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: cheers
 * Time ： 2020/11/7
 * Description ：
 */
public class Inject {
    private static final String TAG = "Inject";
    private static final String SET_CONTENT_VIEW = "setContentView";
    private static final String FIND_VIEW_BY_ID = "findViewById";


    public static void inject(Object context){
        injectContentView(context);
        injectFindById(context);
        injectEvent(context);
    }



    private static void injectFindById(Object context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            BindView annotation = field.getAnnotation(BindView.class);
            if (annotation == null || annotation.value() == 0){
                continue;
            }
            try {
                Method method = clazz.getMethod(FIND_VIEW_BY_ID, int.class);
                Object view = method.invoke(context, annotation.value());
                field.set(context, view);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }


    private static void injectContentView(Object context) {
        Class<?> clazz = context.getClass();
        ContentView annotation = clazz.getAnnotation(ContentView.class);
        if (annotation == null || annotation.value() == -1){
            return;
        }

        try {
            Method method = clazz.getMethod(SET_CONTENT_VIEW, int.class);
            method.invoke(context, annotation.value());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private static void injectEvent(Object context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //获取方法上的所有注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {

                // todo 这里只能用annotationType()， 如果用getClass()将失败
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                BaseEvent eventAnnotation = annotationClass.getAnnotation(BaseEvent.class);
                if (eventAnnotation == null){
                    continue;
                }
                //如果当前注解上使用了BaseEvent这个注解则代表者这是个事件方法
                try {
                    Method valueMethod = annotationClass.getDeclaredMethod("value");
                    // 获取事件方法上的ID数组
                    int[] ids = (int[]) valueMethod.invoke(annotation);
                    if (ids == null || ids.length == 0){
                        continue;
                    }
                    for (int id : ids) {
                        Method findByIdMethod = clazz.getMethod(FIND_VIEW_BY_ID, int.class);
                        Object view = findByIdMethod.invoke(context, id);
                        if (view == null){
                            continue;
                        }

                        // 动态代理的是用户写的点击事件处理方法 因为不知道点击事件的具体实现逻辑
                        EventInvocationHandler invocationHandler = new EventInvocationHandler(context, method);

                        Object proxyInstance = Proxy.newProxyInstance(eventAnnotation.listenerClazz().getClassLoader(), new Class[]{eventAnnotation.listenerClazz()}, invocationHandler);
                        // 获取setOnClickListener（）对象
                        Method listenerMethod = view.getClass().getMethod(eventAnnotation.listenerName(), eventAnnotation.listenerClazz());
                        //将view的点击事件交给代理对象来处理
                        listenerMethod.invoke(view, proxyInstance);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
