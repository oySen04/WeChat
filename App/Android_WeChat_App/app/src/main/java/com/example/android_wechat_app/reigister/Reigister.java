package com.example.android_wechat_app.reigister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_wechat_app.R;
import com.example.android_wechat_app.login.LoginUser;
import com.example.android_wechat_app.start.welcome;
import com.example.android_wechat_app.tools.ButtonColorListenerUtil;
import com.example.android_wechat_app.tools.IEditTextChangeListener;
import com.example.android_wechat_app.tools.WeChatRandomUserName;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reigister extends AppCompatActivity {
    //组件声明
    private EditText username;
    private EditText password;
    private EditText phone;
    private Button button;

    //private static final String IP = "100.66.5.94";

    //随机生成WeChat号码
    private String randomWeChatNumber;

    //自定义UI修改机制
    private AlterHander alterHander = new AlterHander();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //隐藏标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //Android版本5.0，API级别21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //设置全屏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //将状态栏字体设置为黑色
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initViews();
        //设置注册按钮
        if (username.getText() + "" == "" || phone.getText() + "" == "" || password.getText() + "" == "") {
            button.setEnabled(false);
        }else {
            button.setEnabled(true);
        }
        inputChangeFocus();//监听diverse控件变色
        buttonChangeColor();//监听

        //点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern pattern = Pattern.compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\\\d{8}$");
                Matcher matcher = pattern.matcher(phone.getText());
                if (matcher.matches()) {
                    //开辟应该线程完成网络请求操作
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpUrlConPost(Reigister.this.username.getText() + "",phone.getText() + "",password.getText() + "");
                        }
                    }).start();
                }else {
                    Toast.makeText(getApplicationContext(), "手机格式错误", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //获取相应需要用到的控件实例
    @SuppressLint("NewApi")
    public void initViews() {
        //得到所有控件
        username = (EditText) this.findViewById(R.id.reg_username);
        password = (EditText) this.findViewById(R.id.reg_userpasswd);
        phone = (EditText) this.findViewById(R.id.reg_userphone);
        button = (Button) this.findViewById(R.id.reg_button);
    }

    //监听diverse控件变色
    public void inputChangeFocus() {
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    ImageView imageView = (ImageView) findViewById(R.id.reg_diverse1);
                    imageView.setBackgroundResource(R.color.input_diverse_focus);
                }else {
                    ImageView imageView = (ImageView) findViewById(R.id.reg_diverse1);
                    imageView.setBackgroundResource(R.color.input_diverse);
                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    ImageView imageView = (ImageView) findViewById(R.id.reg_diverse3);
                    imageView.setBackgroundResource(R.color.input_diverse_focus);
                }else {
                    ImageView imageView = (ImageView) findViewById(R.id.reg_diverse3);
                    imageView.setBackgroundResource(R.color.input_diverse);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    ImageView imageView = (ImageView) findViewById(R.id.reg_diverse4);
                    imageView.setBackgroundResource(R.color.input_diverse_focus);
                }else {
                    ImageView imageView = (ImageView) findViewById(R.id.reg_diverse4);
                    imageView.setBackgroundResource(R.color.input_diverse);
                }
            }
        });
    }

    //发送请求的方法
    public void httpUrlConPost(String name, String phone, String password) {
        //使用工具类随机生成WeChat号
        WeChatRandomUserName randomUserName = new WeChatRandomUserName();
        randomWeChatNumber = randomUserName.generate();
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("http://100.66.50.142:8080/AndroidServer_war_exploded/Reigister");
            urlConnection = (HttpURLConnection) url.openConnection();// 打开http连接
            urlConnection.setConnectTimeout(3000);// 连接的超时时间
            urlConnection.setUseCaches(false);// 不使用缓存
            urlConnection.setFollowRedirects(false);//是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);// 是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);// 响应的超时时间
            urlConnection.setDoInput(true);// 设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);// 设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");// 设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");// 设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接

            JSONObject json = new JSONObject();// 创建json对象
            json.put("number", URLEncoder.encode(randomWeChatNumber, "UTF-8"));// 使用URLEncoder.encode对特殊和不可见字符进行编码
            json.put("name", URLEncoder.encode(name, "UTF-8"));
            json.put("phone", URLEncoder.encode(phone, "UTF-8"));
            json.put("password", URLEncoder.encode(password, "UTF-8"));// 把数据put进json对象中
            String jsonstr = json.toString();// 把JSON对象按JSON的编码格式转换为字符串
            // ------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();// 输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// 创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);// 把json字符串写入缓冲区中
            bw.flush();// 刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();// 使用完关闭
            Log.i("aa", urlConnection.getResponseCode() + "");
            //以下判斷是否訪問成功，如果返回的状态码是200则说明访问成功
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {// 得到服务端的返回码是否连接成功
                // ------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {// BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());
                Log.i("aa", "rjson=" + rjson);// rjson={"json":true}
                boolean result = rjson.getBoolean("json");// 从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
                System.out.println("json:===" + result);
                //如果服务器端返回的是true，则说明注册成功，否则注册失败
                if (result) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    alterHander.sendEmptyMessage(1);
                    Log.i("用户：", "注册成功");
                } else {
                    alterHander.sendEmptyMessage(2);
                    Log.i("用户：", "手机号已被注册");
                }
            } else {
                alterHander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            alterHander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();// 使用完关闭TCP连接，释放资源
        }
    }

    //监听登录按钮变色
    public void buttonChangeColor() {
        //创建根据类对象，传要改变的button
        ButtonColorListenerUtil.textChangeListener textChangeListener = new ButtonColorListenerUtil.textChangeListener(button);
        textChangeListener.addAllEditText(username,phone,password);
        //回调接口，取到Boolean值,根据isHasContent的值决定 Button应该设置什么颜色
        ButtonColorListenerUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    button.setBackgroundResource(R.drawable.login_button_focus);
                    button.setTextColor(getResources().getColor(R.color.loginButtonTextFouse));
                }else {
                    button.setBackgroundResource(R.drawable.login_button_shape);
                    button.setTextColor(getResources().getColor(R.color.loginButtonText));
                }
            }
        });
    }
    //Handler机制来完成对UI的操作,Android中不允许在线程直接修改UI
    class AlterHander extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //判断hander的内容是什么，1则说明注册成功，2说明注册失败
            switch (msg.what) {
                //注册成功
                case 1:
                    Log.i("注册成功",msg.what + "");
//                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
//                    //跳转至登录界面，并传送WeChat号
//                    Intent intent = new Intent();
//                    intent.putExtra("weixin_number",randomWeChatNumber);
//                    intent.setClass(Reigister.this, LoginUser.class);
//                    startActivity(intent);
//                    Reigister.this.finish();//结束当前activity
                    break;
                //注册失败
                case 2:
                    Log.i("注册失败",msg.what + "");
                    Toast.makeText(getApplicationContext(),"手机号已被注册，请重新输入",Toast.LENGTH_LONG).show();
            }
        }
    }

    //返回按钮处理事件
    public void rigister_activity_back(View v) {
        //跳转到微信启动页
        Intent intent = new Intent();
        intent.setClass(Reigister.this, welcome.class);
        startActivity(intent);
        Reigister.this.finish(); //结束当前activity
    }
}
