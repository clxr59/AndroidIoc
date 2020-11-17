package com.hankage.hrouter.bean;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.hankage.hrouter.HRouter;
import com.hankage.hrouter_annotation.model.RouteMeta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author: cheers
 * Time ： 11/17/20
 * Description ：
 */
public final class Postcard extends RouteMeta {
    private Bundle mBundle;
    private int flags = -1;

    public Postcard(String group, String path) {
        this(group, path, null);
    }

    public Postcard(String group, String path, Bundle bundle) {
        super(group, path);
        this.mBundle = bundle == null ? new Bundle() : bundle;
    }

    public Postcard withFlags(int flag) {
        this.flags = flag;
        return this;
    }

    public Postcard addFlags(int flags) {
        this.flags |= flags;
        return this;
    }

    public int getFlags() {
        return flags;
    }


    public Postcard withBundle(Bundle bundle) {
        if (bundle != null) {
            this.mBundle = bundle;
        }

        return this;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public Postcard withBoolean(@Nullable String key, boolean val) {
        this.mBundle.putBoolean(key, val);
        return this;
    }

    public Postcard withChar(@Nullable String key, char val) {
        this.mBundle.putChar(key, val);
        return this;
    }


    public Postcard withShort(@Nullable String key, short val) {
        this.mBundle.putShort(key, val);
        return this;
    }

    public Postcard withInt(@Nullable String key, int val) {
        this.mBundle.putInt(key, val);
        return this;
    }

    public Postcard withLong(@Nullable String key, long val) {
        this.mBundle.putLong(key, val);
        return this;
    }

    public Postcard withFloat(@Nullable String key, float val) {
        this.mBundle.putFloat(key, val);
        return this;
    }

    public Postcard withDouble(@Nullable String key, double val) {
        this.mBundle.putDouble(key, val);
        return this;
    }

    public Postcard withString(@Nullable String key, String val) {
        this.mBundle.putString(key, val);
        return this;
    }

    public Postcard withByte(@Nullable String key, byte val) {
        this.mBundle.putByte(key, val);
        return this;
    }

    public Postcard withSerializable(@Nullable String key, @Nullable Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public Postcard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    public Postcard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public Postcard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public Postcard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public Postcard withByteArray(@Nullable String key, @Nullable byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }
    public Postcard withShortArray(@Nullable String key, @Nullable short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }
    public Postcard withCharArray(@Nullable String key, @Nullable char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }

    public Postcard withFloatArray(@Nullable String key, @Nullable float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }



    public void navigation(){
        HRouter.getInstance().navigation(null, this, -1);
    }


    public void navigation(Context context){
        HRouter.getInstance().navigation(context, this, -1);
    }


    public void navigation(Activity mContext, int requestCode) {
        HRouter.getInstance().navigation(mContext, this, requestCode);
    }

}
