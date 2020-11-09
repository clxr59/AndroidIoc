package com.hankage.butterknife;

import androidx.annotation.UiThread;

/**
 * Author: cheers
 * Time ： 2020/11/9
 * Description ：
 */
public interface Unbinder {
    @UiThread
    void unBinder();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unBinder() {

        }
    };
}
