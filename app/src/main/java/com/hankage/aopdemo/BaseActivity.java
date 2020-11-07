package com.hankage.aopdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hankage.viewbing.Inject;

/**
 * Author: cheers
 * Time ： 2020/11/7
 * Description ：
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.inject(this);
    }
}
