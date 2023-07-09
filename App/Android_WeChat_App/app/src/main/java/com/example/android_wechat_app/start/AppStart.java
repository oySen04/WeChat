package com.example.android_wechat_app.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import com.example.android_wechat_app.R;

public class AppStart extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start);

        //延迟跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转至微信注册与登录界面
                Intent intent = new Intent(AppStart.this, welcome.class);
                startActivity(intent);
                AppStart.this.finish();
            }
        },1000);
    }
}
