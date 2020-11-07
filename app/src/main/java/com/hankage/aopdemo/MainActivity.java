package com.hankage.aopdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hankage.viewbing.annotation.BindView;
import com.hankage.viewbing.annotation.ContentView;
import com.hankage.viewbing.annotation.OnClick;
import com.hankage.viewbing.annotation.OnLongClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @BindView(R.id.btn1)
    Button btn1;

    @BindView(R.id.btn2)
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn1.setText("按钮 1");
        btn2.setText("Button 2");

    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onClick(View view){
        Toast.makeText(this, "点击了 " + view.getId(), Toast.LENGTH_LONG).show();
    }

    @OnLongClick({R.id.btn2})
    public boolean onLongClick(View view){
        Toast.makeText(this, "长按了 " + view.getId(), Toast.LENGTH_LONG).show();
        return true;
    }
}