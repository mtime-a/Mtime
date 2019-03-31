package com.example.mtimeapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Credentials;
import android.net.Network;
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

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Authenticator;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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
import okhttp3.Route;

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
    private String state;
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
//                SharedPreferences sps = getSharedPreferences("cookie", Context.MODE_PRIVATE);
//                String name = sps.getString("cookie","" );
//                Log.e("TAG",name);
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
                    Headers headers = response.headers();
                    String responseData = response.body().string();
                    Log.e("TAG",responseData);
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
            Log.e("TAG",verify_id+"验证码ID");
            //等待时间（未解决）
            waitTime = jsonObject.getString("wait");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void postRegJsonData(){
        try {
            JSONObject body = new JSONObject();
            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("theName", name);
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
            body.put("email", email);
            body.put("user_name", name);
            body.put("password", password);
            //
            body.put("verify_id", "123456");
            //
            body.put("verify_code", code);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(body));
            Request request = new Request.Builder()
                    .url("http://106.13.106.1/account/i/register/")
                    .post(requestBody)
                    .build();
            okHttpClient  = new OkHttpClient.Builder()
                    .connectTimeout(60*60,TimeUnit.SECONDS)
                    .cookieJar(cookieJar)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "获取数据失败");
                }
                @Override
                public void onResponse(Call call,Response response) throws IOException {
                    if(response.isSuccessful()){
                       state = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(state);
                                    String status = jsonObject.getString("result");
                                    judgeRegState(status);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
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
    private void judgeRegState(String state){
//        0:注册成功
//        1:用户ID重复
//        2:电子邮件已被注册
//        3:验证码错误
//        4:无效的昵称
//        5:无效的密码
//        6:未知错误
//        7:无效的用户ID
//        8:注册数据不完整
//        9:josn格式错误
        if(state.equals("0") ){
            Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
            finish();
        }
        if(state.equals("1") ){
            Toast.makeText(this,"用户名重复,请重新选择用户名",Toast.LENGTH_LONG).show();
        }
        if(state.equals("2") ){
            Toast.makeText(this,"电子邮箱已被注册",Toast.LENGTH_LONG).show();
        }
        if(state.equals("3") ){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state.equals("4") ){
            Toast.makeText(this,"无效的用户名",Toast.LENGTH_LONG).show();
        }
        if(state.equals("5")){
            Toast.makeText(this,"无效的密码",Toast.LENGTH_LONG).show();
        }
        if(state.equals("6")){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }

    private void postLogJsonData() {
        account = log_account.getText().toString();
        password = log_password.getText().toString();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject body = new JSONObject();
            body.put("user_key", account);
            body.put("key_type","user_id");
            body.put("password", password);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(body));
            Request request = new Request.Builder()
                    .url("http://106.13.106.1/account/i/login/")
                    .post(requestBody)
                    .build();
            okHttpClient  = new OkHttpClient.Builder()
                    .connectTimeout(60*60,TimeUnit.SECONDS)
                    .cookieJar(cookieJar)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "获取数据失败");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int state;
                    final String response1 = response.body().string();
                    Log.e("TAG", response1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(response1);
                                String status = jsonObject.getString("result");
                                judgeLogState(status);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        private void judgeLogState(String state){
//            0:登陆成功
//            1:无效用户ID
//            2:无效的密码
//            3:验证码错误
//            4:账号被封禁
//            5:已登录
//            6：未知错误
//            8: 登陆数据缺失
//            9:json格式错误
        if(state.equals("0") ){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
            finish();
        }
        if(state.equals("1")){
            Toast.makeText(this,"无效用户名",Toast.LENGTH_LONG).show();
        }
        if(state.equals("2")){
            Toast.makeText(this,"无效的密码",Toast.LENGTH_LONG).show();
        }
        if(state.equals("3")){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state.equals("4")){
            Toast.makeText(this,"账号被封禁",Toast.LENGTH_LONG).show();
        }
        if(state.equals("5")){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
        }
        if(state.equals("6")){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }
    CookieJar cookieJar = new CookieJar() {
        private final Map<String, List<Cookie>> cookiesMap = new HashMap<String, List<Cookie>>();
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            String host = url.host();
            List<Cookie> cookiesList = cookiesMap.get(host);
            if (cookiesList != null){
                cookiesMap.remove(host);
            }
            for(Cookie cookie : cookies){
                Log.e("TAG saveFromResponse",cookie.toString());
                SharedPreferences sps = getSharedPreferences("cookie", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sps.edit();
                editor.putString("cookies", cookie.toString());
                editor.commit();
            }
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookiesList = cookiesMap.get(url.host());
            return cookiesList != null ? cookiesList : new ArrayList<Cookie>();
        }
    };
}
