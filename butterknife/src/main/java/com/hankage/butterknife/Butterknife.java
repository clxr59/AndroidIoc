package com.hankage.butterknife;

import android.app.Activity;
import android.view.View;

import androidx.annotation.VisibleForTesting;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: cheers
 * Time ： 2020/11/9
 * Description ：
 */
public final class Butterknife {

    @VisibleForTesting
    static final Map<Class<?>, Constructor<? extends Unbinder>> BINDINGS = new LinkedHashMap<>();

    public static Unbinder binder(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        return binder(decorView, activity);
    }

    public static Unbinder binder(View view, Object target){
        Class<?> clazz = target.getClass();
        Constructor<? extends Unbinder> constructor = findBindingConstructorForClass(clazz);
        if (constructor == null){
            return Unbinder.EMPTY;
        }
        try {
            return constructor.newInstance(target, view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Unbinder.EMPTY;
    }

    private static Constructor<? extends Unbinder> findBindingConstructorForClass(Class<?> clazz) {
        Constructor<? extends Unbinder> constructor = BINDINGS.get(clazz);
        if (constructor != null){
            return constructor;
        }

        try {
            Class<?> loadClass = clazz.getClassLoader().loadClass(clazz.getName() + "_ViewBinding");
            constructor = (Constructor<? extends Unbinder>) loadClass.getConstructor(clazz, View.class);
            BINDINGS.put(clazz, constructor);
            return constructor;

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
