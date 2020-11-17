package com.hankage.hrouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.hankage.hrouter.bean.Postcard;
import com.hankage.hrouter.temlate.IRouteGroup;
import com.hankage.hrouter.temlate.IRouteRoot;
import com.hankage.hrouter.util.HRouterConstant;
import com.hankage.hrouter_annotation.model.RouteMeta;

import java.util.Map;

/**
 * Author: cheers
 * Time ： 11/17/20
 * Description ：
 */
public class _HRouter {
    private static volatile _HRouter sInstance = null;
    private static Context mContext;

    private LruCache<String, IRouteRoot> mRootCache;
    private LruCache<String, IRouteGroup> mGroupCache;


    public static synchronized void init(Application app){
        mContext = app;
    }

    public static _HRouter getInstance() {
        if (sInstance == null){
            synchronized (_HRouter.class){
                if (sInstance == null){
                    sInstance = new _HRouter();
                }
            }
        }
        return sInstance;
    }


    private  _HRouter() {
        mRootCache = new LruCache<>(32);
        mGroupCache = new LruCache<>(128);
    }

    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)){
            throw new IllegalArgumentException(HRouterConstant.TAG + "path 不能为空");
        }
        String group = extractGroup(path);
        return build(path, group);
    }


    public Postcard build(String path, String group){
        if (TextUtils.isEmpty(group) || TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException(HRouterConstant.TAG + "path 的格式不对，标准格式为： /xx/xx");
        }

        return new Postcard(group, path);
    }



    /**
     * 从path中获取group
     * @param path path
     * @return group
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/") || path.lastIndexOf("/") == 0){
            throw new IllegalArgumentException(HRouterConstant.TAG + "path 的格式不对，标准格式为： /xx/xx");
        }

        try {
           return path.substring(1, path.indexOf("/", 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void navigation(Context context, Postcard postcard, int requestCode) {

        IRouteGroup routeGroup = mGroupCache.get(postcard.getGroup());
        if (routeGroup == null){
            routeGroup = getRouteGroupFromGroupCache(postcard.getGroup());
        }

        if (routeGroup == null){
            throw new IllegalStateException(HRouterConstant.TAG + "group 对应的文件不存在");
        }

        Map<String, RouteMeta> map = routeGroup.loadInto();
        if (map.isEmpty()){
            throw new IllegalStateException(HRouterConstant.TAG + "group 对应的文件不存在");
        }

        RouteMeta meta = map.get(postcard.getPath());
        if (meta == null || !postcard.getPath().equals(meta.getPath())){
            throw new IllegalArgumentException(HRouterConstant.TAG + "path 路径不存在");
        }

        postcard.setDestination(meta.getDestination());
        postcard.setRouteType(meta.getRouteType());
        dispatcherRouter(context, postcard, requestCode);

    }

    /**
     * 进行router 分发
     * @param context  context
     * @param postcard postcard
     * @param requestCode requestCode
     */
    private void dispatcherRouter(Context context, Postcard postcard, int requestCode) {
        switch (postcard.getRouteType()){
            case ACTIVITY:
                startActivity(context, postcard, requestCode);
                break;
            case SERVICE:
                break;
            case UNKNOWN:
                break;
        }
    }

    private void startActivity(Context context, Postcard postcard, int requestCode) {
        if (context == null){
            context = mContext;
        }

        Intent intent = new Intent(context, postcard.getDestination());
        intent.addFlags(postcard.getFlags());
        intent.putExtras(postcard.getBundle());
        if (requestCode != -1){
            ((Activity) context).startActivityForResult(intent, requestCode);
            return;
        }

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 获取 HRouter$$Group$$xx对象
     * @param group xxx
     * @return HRouter$$Group$$对象
     */
    private IRouteGroup getRouteGroupFromGroupCache(String group) {
        String className = HRouterConstant.ROUTER_ROOT_PACKAGE_NAME + group;
        IRouteRoot routeRoot = mRootCache.get(className);
        if (routeRoot == null){
            routeRoot = getRouteRootFromClass(className);
        }
        Map<String, Class<? extends IRouteGroup>> map = routeRoot.loadInfo();
        if (map.isEmpty()){
            throw new IllegalArgumentException(HRouterConstant.TAG + "Root map is empty");
        }

        IRouteGroup routeGroup = null;
        try {
            Class<? extends IRouteGroup> clazz = map.get(group);
            routeGroup = (IRouteGroup) clazz.newInstance();
            mGroupCache.put(group, routeGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeGroup;
    }


    /**
     * 通过类加载方式获取 HRouter$$Root$$xx 文件
     * @param className HRouter$$Root$$xx文件全名称
     * @return HRouter$$Root$$xx 对象
     */
    private IRouteRoot getRouteRootFromClass(String className) {
        Log.i(HRouterConstant.TAG, "getRouteRootFromClass: " + className);
        IRouteRoot routeRoot = null;
        try {
            Class<?> clazz = Class.forName(className);
            Log.i(HRouterConstant.TAG, "getRouteRootFromClass: " + clazz.newInstance());
            routeRoot = (IRouteRoot) clazz.newInstance();
            mRootCache.put(className, routeRoot);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return routeRoot;
    }
}
