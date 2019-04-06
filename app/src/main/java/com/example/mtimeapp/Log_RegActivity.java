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

import com.example.mtimeapp.Fragment.Fragment_PC;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okhttp3.Cookie;


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

    private String password = null;
    private String account = null;
    private String msg;
    private String email;
    private String code;
    private String name;
    private String nickName;
    private String headImageUrl;
    private String statu;
    private String session;
    private String username;


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
                log_account.setText("");
                log_password.setText("");
                break;

            case R.id.reg_send:
                email = reg_mail.getText().toString();
                Toast.makeText(Log_RegActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                RequestCode();
                break;
            case R.id.reg_btn:                                  //注册
                code = reg_code.getText().toString();
                password = reg_password.getText().toString();
                name = reg_account.getText().toString();
                email = reg_mail.getText().toString();

                if (checkCode(password)) {
                    if (code == null || code.equals("")) {
                        Toast.makeText(this, "请填入验证码", Toast.LENGTH_LONG).show();
                    } else {
                        postRegJsonData();
                    }
                } else {
                    Toast.makeText(this, "请填入正确格式的密码", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.reg_find_password:
                intent.setClass(Log_RegActivity.this, FindPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.reg_switch:
                reg.setVisibility(View.GONE);
                log.setVisibility(View.VISIBLE);
                reg_code.setText("");//恢复原始数据
                reg_mail.setText("");
                reg_account.setText("");
                reg_password.setText("");
                break;
        }
    }

    //请求验证码
    private void RequestCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(100, TimeUnit.SECONDS)
                            .writeTimeout(100, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .build();

                    Request request = new Request.Builder()
                            .addHeader("Connection","close")
                            .url("http://132.232.78.106:8001/api/sendCheckCode/")
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject_Code(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    RequestCode();//如果请求失败就继续请求
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject_Code(String JsonData) {
        try {
            JSONArray jsonArray = new JSONArray(JsonData);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String status = jsonObject.getString("statu");
            String msg = jsonObject.getString("msg");

//            code = jsonObject.getString("code");//全局变量要让注册状态时候的code有数据
//
//            showResponse(code);
            judgeCodeState(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void judgeCodeState(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Log_RegActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void showResponse(final String code) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                reg_code.setText(code);
//            }
//        });
//    }

    //发送注册信息
    private void postRegJsonData() {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("username", name)
                .add("email", email)
                .add("password", password)
                .add("nickname", name)
                .add("vericode", code)
                .build();
        Request request = new Request.Builder()
                .url("http://132.232.78.106:8001/api/register/")//请求的url
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("mlj", "获取数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        statu = jsonObject.getString("state");
                        String msg = jsonObject.getString("msg");
                        judgeRegState(statu);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    //判断注册返回状态
    private void judgeRegState(final String state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state.equals("1")) {
                    Toast.makeText(Log_RegActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(Log_RegActivity.this, Log_RegActivity.class);
                    startActivity(intent);
                    finish();
                } else if (state.equals("-1"))
                    Toast.makeText(Log_RegActivity.this, "验证码过期", Toast.LENGTH_LONG).show();
                else if (state.equals("-2"))
                    Toast.makeText(Log_RegActivity.this, "验证码错误", Toast.LENGTH_LONG).show();
                else if (state.equals("404"))
                    Toast.makeText(Log_RegActivity.this, "非法请求", Toast.LENGTH_LONG).show();
                else Toast.makeText(Log_RegActivity.this, "未知错误", Toast.LENGTH_LONG).show();
            }
        });
    }

    //发送登录请求
    private void postLogJsonData() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("username", account)
                .add("password", password)
                .build();

        Log.e("Log",account + password);
        Request request = new Request.Builder()
                .url("http://132.232.78.106:8001/api/login/")
                .post(formBody)
                .addHeader("Connection","close")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "获取数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("Log_Reg",responseData);
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        statu = jsonObject.getString("statu");
                        if(statu.equals("1")) {
                            session = jsonObject.getString("session");
                            nickName = jsonObject.getString("nickName");
                            email = jsonObject.getString("email");
                            username = jsonObject.getString("username");
                            headImageUrl = jsonObject.getString("headImage");

                            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("cookie",session);
                            editor.putString("theName", username);
                            editor.putString("theNickname", nickName);
                            editor.putString("theHeadImage", headImageUrl);
                            editor.putString("theEmail", email);
                            editor.apply();
                        }
                        else {
                            String msg = jsonObject.getString("msg");
                            Log.e("Log_Reg",msg);
                        }
                        judgeLogState(statu);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //判断登录状态
    private void judgeLogState(final String state) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state.equals("1")) {
                    Toast.makeText(Log_RegActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(Log_RegActivity.this, PCActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("username", username);
//                    bundle.putString("nickName", nickName);
//                    bundle.putString("headImage", headImageUrl);
//                    intent.putExtra("UserMessage",bundle);
                    startActivity(intent);
                    finish();
                }
                if(state.equals("-1")){
                    Toast.makeText(Log_RegActivity.this, "账号不存在,请先注册", Toast.LENGTH_LONG).show();
                }
                if(state.equals("-2")){
                    Toast.makeText(Log_RegActivity.this, "密码不正确", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //检验密码
    private boolean checkCode(String code) {
        //限制密码
        Pattern pattern = Pattern.compile("[\\S]{8,16}$");
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }
}
