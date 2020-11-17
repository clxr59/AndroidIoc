package com.hankage.hrouter;

import android.app.Application;
import android.content.Context;

import com.hankage.hrouter.bean.Postcard;

/**
 * Author: cheers
 * Time ： 2020/11/16
 * Description ：
 */
public final class HRouter {
    private static volatile HRouter sInstance = null;
    private static volatile boolean isInit = false;

    public static void init(Application app){
        if (!isInit){
            _HRouter.init(app);
            isInit = true;
        }
    }

    public static HRouter getInstance() {
        if (sInstance == null){
            synchronized (HRouter.class){
                if (sInstance == null){
                    sInstance = new HRouter();
                }
            }
        }
        return sInstance;
    }


    public Postcard build(String path){
        if (!isInit){
            throw new IllegalStateException("HRouter 没有进行初始化");
        }
        return _HRouter.getInstance().build(path);
    }

    public Postcard build(String path, String group){
        if (!isInit){
            throw new IllegalStateException("HRouter 没有进行初始化");
        }
        return _HRouter.getInstance().build(path, group);
    }


    public void navigation(Context context, Postcard postcard, int requestCode) {
        _HRouter.getInstance().navigation(context, postcard, requestCode);
    }
}
