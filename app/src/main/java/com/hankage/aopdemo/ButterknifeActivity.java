package com.hankage.aopdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hankage.butterknife.Butterknife;
import com.hankage.butterknife_annotation.OnClick;
import com.hankage.hrouter_annotation.HRouter;

@HRouter(path = "/app/ButterknifeActivity")
public class ButterknifeActivity extends BaseActivity {

//    @BindView(R.id.btn1)
//    Button btn1;
//
//    @BindView(R.id.btn2)
//    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butterknife);
        Butterknife.binder(this);
//        btn1.setText("hhahahahhaha");
    }


    @OnClick({R.id.btn2})
    public void onClickView(View view){
        Toast.makeText(ButterknifeActivity.this, "点击啦", Toast.LENGTH_LONG).show();
    }
}