package com.hankage.login;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hankage.hrouter_annotation.HRouter;


@HRouter(path = "/login/LoginActivity")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       TextView tv_info =  findViewById(R.id.tv_info);
       tv_info.setText(getIntent().getStringExtra("info"));
    }
}