package com.example.mtimeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Log_RegActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout reg;
    private EditText reg_account;
    private EditText reg_password;
    private EditText reg_mail;
    private EditText reg_code;
    private ImageView reg_send;
    private Button reg_btn;
    private TextView reg_switch;
    private TextView reg_find_password;

    private LinearLayout log;
    private EditText log_account;
    private EditText log_password;
    private Button log_btn;
    private TextView log_switch;
    private TextView log_find_password;

    private ImageView close;

    private String password;
    private String account;
    private String verify_id = null;
//    private String UserId;
    private String email;
    private String code;
    private String name;
    private String waitTime;
    private String cookies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        initUI();

        reg.setVisibility(View.GONE);

        close.setOnClickListener(this);

        initLog();

        initReg();
    }

    private void initReg() {
        reg_switch.setOnClickListener(this);
        reg_find_password.setOnClickListener(this);
        reg_btn.setOnClickListener(this);
        reg_send.setOnClickListener(this);
    }


    private void initLog() {
        log_switch.setOnClickListener(this);
        log_find_password.setOnClickListener(this);
        log_btn.setOnClickListener(this);
    }

    private void initUI() {

        close = findViewById(R.id.close);
        //下面是关于reg的监听
        reg = findViewById(R.id.reg);
        reg_account = findViewById(R.id.reg_account);
        reg_password = findViewById(R.id.reg_password);
        reg_mail = findViewById(R.id.reg_mail);
        reg_code = findViewById(R.id.reg_code);
        reg_send = findViewById(R.id.reg_send);
        reg_switch = findViewById(R.id.reg_switch);
        reg_find_password = findViewById(R.id.reg_find_password);
        reg_btn = findViewById(R.id.reg_btn);
        //下面是关于log的监听
        log = findViewById(R.id.log);
        log_account = findViewById(R.id.log_account);
        log_password = findViewById(R.id.log_password);
        log_switch = findViewById(R.id.log_switch);
        log_find_password = findViewById(R.id.log_find_password);
        log_btn = findViewById(R.id.log_btn);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.log_btn:
               account = log_account.getText().toString();
               password = log_password.getText().toString();
               postLogJsonData();
                break;
            case R.id.log_find_password:
                intent.setClass(Log_RegActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.log_switch:
                log.setVisibility(View.GONE);
                reg.setVisibility(View.VISIBLE);
                break;
            case R.id.reg_send:
                initThread();
                break;
            case R.id.reg_btn:                                  //注册
                code = reg_code.getText().toString();
                password = reg_password.getText().toString();
                name  = reg_account.getText().toString();
                email  = reg_mail.getText().toString();
                boolean judge = checkCode(password);
                if(judge){
//                    if(verify_id == null||verify_id.equals("")){
//                        //***********************************
//                        //可以把Toast做的好看一些
//                        //***********************************
//                        Toast.makeText(this,"请先获取验证码",Toast.LENGTH_LONG).show();
//                    }else {
                        if(code == null||code.equals("")){
                            Toast.makeText(this,"请填入验证码",Toast.LENGTH_LONG).show();
                        }else {
                            postRegJsonData();
                     //   }
                    }
                }else{
                    Toast.makeText(this,"请填入正确格式的密码",Toast.LENGTH_LONG).show();
                }
//            {
//                "user_id": "用户id",
//                    "user_name": "用户名",
//                    "password": "经过加密的密码（加密算法待定）",
//                    "verify_id": "验证码id",
//                    "verify_code": "验证码值"
//            }

                break;
            case R.id.reg_find_password:
                intent.setClass(Log_RegActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.reg_switch:
                reg.setVisibility(View.GONE);
                log.setVisibility(View.VISIBLE);
                break;
        }
    }
    //请求验证码
    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("TAG","2");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://106.13.106.1/i/email_verify_code/").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("TAG",responseData);
                    //解析返回值
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            //验证码id
            verify_id = jsonObject.getString("id");
            //等待时间（未解决）
            waitTime = jsonObject.getString("wait");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void postRegJsonData(){
        try {
            JSONObject body = new JSONObject();
            Log.e("TAG",name);
            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("theName",name);
            editor.apply();
//            {
//                "user_id": "用户id",
//                    "email":"email",
//                    "user_name": "用户名",
//                    "password": "经过加密的密码（加密算法待定）",
//                    "verify_id": "验证码id",
//                    "verify_code": "验证码值"
//            }
            body.put("user_id", name);
            body.put("email",email);
            body.put("user_name", name);
            body.put("password", password);
            //
            body.put("verify_id", "123456");
            //
            body.put("verify_code", code);
            Log.e("TAG", String.valueOf(body));
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(body));
            Request request = new Request.Builder()
                    .url("http://106.13.106.1/account/i/register/")
                    .post(requestBody)
                    .build();
            Log.e("TAG","12");

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "获取数据失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int state;
                    Log.e("TAG", String.valueOf(response));
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.toString());
//                        state  = Integer.parseInt(jsonObject.getString("result"));
//                        judgeRegState(state);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            } );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //检验密码
    public static boolean checkCode(String code){
        Pattern pattern=Pattern.compile("[\\S]{8,16}$");
        Matcher matcher=pattern.matcher(code);
        return matcher.matches();
    }
    //判断返回状态
    private void judgeRegState(int state){
//        0:注册成功
//        1:用户名重复
//        2:电子邮件已被注册
//        3:验证码错误
//        4:无效的用户名
//        5:无效的密码
//        6:未知错误
        if(state == 0 ){
            Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
        }
        if(state == 1 ){
            Toast.makeText(this,"用户名重复,请重新选择用户名",Toast.LENGTH_LONG).show();
        }
        if(state == 2 ){
            Toast.makeText(this,"电子邮箱已被注册",Toast.LENGTH_LONG).show();
        }
        if(state == 3 ){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state == 4 ){
            Toast.makeText(this,"无效的用户名",Toast.LENGTH_LONG).show();
        }
        if(state == 5 ){
            Toast.makeText(this,"无效的密码",Toast.LENGTH_LONG).show();
        }
        if(state == 6 ){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }

    private void postLogJsonData() {
        account = log_account.getText().toString();
        password = log_password.getText().toString();
        try {
//  "account":"用户名",
//  "email":"电子邮件",
//  "password":"加密后的密码(加密算法待定)",
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject body = new JSONObject();
            body.put("account", account);
            body.put("password", password);
            final OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(body));
            final Request request = new Request.Builder()
                    .url("http://106.13.106.1/account/i/app_login/")
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "获取数据失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int state;
                    Log.e("TAG", String.valueOf(response));
                        Log.e("TAG","ouhuo");
                        Log.e("TAG",request.body().toString());
                        Log.e("TAG",response.header("Set-Cookie").toString());
                        Response response1 = okHttpClient.newCall(request).execute();
                        cookies  = response.header("Set-Cookie");
                        Log.e("TAG",cookies);
                        //Headers headers = response.headers();
//                        List<String> cookies = headers.get();
//                        Headers headers = response.headers();
//                        List<String> cookies=headers.values("Set-Cookie");
//                        Log.e("TAG", String.valueOf(cookies));
//                    if(cookies.size()>0){
//                        String session=cookies.get(0);
//                        String result=session.substring(0,session.indexOf(";"));
//                        CliniciansApplication.setOkhttpCookie(result);
//                    }

//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                        state =  jsonObject.getInt("result");
//                        judgeLogState(state);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        private void judgeLogState(int state){
//        0:登陆成功
//        1:无效用户名
//        2:无效的密码
//        3:验证码错误
//        4:账号被封禁
//        5:已登录
//        6：未知错误
        if(state == 0 ){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
        }
        if(state == 1 ){
            Toast.makeText(this,"无效用户名",Toast.LENGTH_LONG).show();
        }
        if(state == 2 ){
            Toast.makeText(this,"无效的密码",Toast.LENGTH_LONG).show();
        }
        if(state == 3 ){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state == 4 ){
            Toast.makeText(this,"账号被封禁",Toast.LENGTH_LONG).show();
        }
        if(state == 5 ){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
        }
        if(state == 6 ){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }
}
