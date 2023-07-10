package com.example.android_wechat_app.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.android_wechat_app.R;
import com.example.android_wechat_app.login.LoginUser;
import com.example.android_wechat_app.reigister.Reigister;


public class welcome extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    //登录点击事件
    public void welcome_login(View v) {
        Intent intent = new Intent();
         //跳转登录界面
        intent.setClass(welcome.this, LoginUser.class);
        startActivity(intent);
        this.finish();
    }

    public void welcome_register(View v) {
        Intent intent = new Intent();
        intent.setClass(welcome.this, Reigister.class);
        startActivity(intent);
        this.finish();//结束当前activity
    }
}
