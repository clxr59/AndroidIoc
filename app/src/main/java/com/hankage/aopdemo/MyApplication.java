package com.hankage.aopdemo;

import android.app.Application;

import com.hankage.hrouter.HRouter;

/**
 * Author: cheers
 * Time ： 11/17/20
 * Description ：
 */
public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HRouter.init(this);
    }
}
