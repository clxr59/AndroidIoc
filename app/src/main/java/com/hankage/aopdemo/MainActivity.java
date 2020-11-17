package com.hankage.aopdemo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hankage.hrouter.HRouter;

/**
 * Author: cheers
 * Time ： 2020/11/9
 * Description ：
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
//                startActivity(new Intent(MainActivity.this, ButterknifeActivity.class));
                HRouter.getInstance().build("/login/LoginActivity")
                        .withString("info", "来自 /app/MainActivity 的数据")
                        .navigation(this);
//                HRouter.getInstance().build("/app/ButterknifeActivity").navigation(this);
                break;

        }
    }
}
