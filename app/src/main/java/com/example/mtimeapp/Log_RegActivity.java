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

    private String password;
    private String account;
    private String msg;
    private String email;
    private String code;
    private String name;
    private String nickName;
    private String headImageUrl;
    private String statu;
    private String session;


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
                SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("theName", account);
                editor.apply();
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
                email = reg_mail.getText().toString();
                initThread();
                break;
            case R.id.reg_btn:                                  //注册
                code = reg_code.getText().toString();
                password = reg_password.getText().toString();
                name  = reg_account.getText().toString();
                email  = reg_mail.getText().toString();
                SharedPreferences sharedPreferences1 = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("theName", name);
                editor1.apply();
                boolean judge = checkCode(password);
                if(judge){
                        if(code == null||code.equals("")){
                            Toast.makeText(this,"请填入验证码",Toast.LENGTH_LONG).show();
                        }else {
                            postRegJsonData();
                    }
                }else{
                    Toast.makeText(this,"请填入正确格式的密码",Toast.LENGTH_LONG).show();
                }

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
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/sendCheckCode/")//请求的url
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
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
            Log.e("TAG","解析json");
            JSONArray jsonArray = new JSONArray(JsonData);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            //**************************************
            // 还有用
            //  String status = jsonObject.getString("statu");
            String code = jsonObject.getString("code");
            Log.e("Code",code);
            msg = jsonObject.getString("msg");
            Log.e("TAG",msg);
            showResponse(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponse(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void postRegJsonData(){

//            JSONObject body = new JSONObject();
            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("theName", name);
            editor.apply();
            OkHttpClient okHttpClient  = new OkHttpClient();
            FormBody formBody = new FormBody.Builder()
                    .add("username",name)
                    .add("email", email)
                    .add("password", password)
                    .add("nickname",name)
                    .add("vericode",code)
                    .build();
            final Request request = new Request.Builder()
                    .url("http://132.232.78.106:8001/api/register/")//请求的url
                    .post(formBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                //请求错误回调方法
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG","获取数据失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            statu = jsonObject.getString("state");
                            session = jsonObject.getString("session");
                            String msg = jsonObject.getString("msg");

                            SharedPreferences sps = getSharedPreferences ("Cookies", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sps.edit();
                            editor.putString("cookie",session);
                            editor.apply();

                            showResponse(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        judgeRegState(statu);
                        postLogJsonData();
                    }
                }
            });

    }
    //检验密码
    public static boolean checkCode(String code){
        Pattern pattern=Pattern.compile("[\\S]{8,16}$");
        Matcher matcher=pattern.matcher(code);
        return matcher.matches();
    }
    //判断注册返回状态
    private void judgeRegState(final String state){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state.equals("1")){
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
                    SharedPreferences sps = getSharedPreferences("Cookies", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sps.edit();
                    editor.putString("cookie", session);
                    editor.apply();
                    finish();
                }
                if(state.equals("-1")){
                    Toast.makeText(getApplicationContext(),"验证码过期",Toast.LENGTH_LONG).show();
                }
                if(state.equals("-2")){
                    Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_LONG).show();
                }
                if(state.equals("404")){
                    Toast.makeText(getApplicationContext(),"非法请求",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
//发送登录请求
    private void postLogJsonData() {

        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("username",account)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url("http://132.232.78.106:8001/api/login/")
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "获取数据失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        statu = jsonObject.getString("statu");
                        session = jsonObject.getString("session");
                        nickName = jsonObject.getString("nickName");
                        name = jsonObject.getString("username");
                        headImageUrl = jsonObject.getString("headImage");

//                        Fragment_PC fragment_pc = new Fragment_PC();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("username",username);
//                        bundle.putString("nickName",nickName);
//                        bundle.putString("headImage",headImageUrl);
//                        fragment_pc.setArguments(bundle);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    judgeLogState(statu);
                }
            }
        });
    }
    //判断登录状态
    private void judgeLogState(String state){
        if(state.equals("1")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_LONG).show();
                    SharedPreferences sps = getSharedPreferences("Cookies", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sps.edit();
                    editor.putString("cookie", session);
                    editor.apply();
                    Intent intent = new Intent(Log_RegActivity.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",name);
                    bundle.putString("nickName",nickName);
                    bundle.putString("headImage",headImageUrl);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            finish();
        }
    }
//    CookieJar cookieJar = new CookieJar() {
//        @Override
//        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//            String host = url.host();
//            List<Cookie> cookiesList = cookiesMap.get(host);
//            if (cookiesList != null){
//                cookiesMap.remove(host);
//            }
//            for(Cookie cookie : cookies){
//                Log.e("TAG saveFromResponse",cookie.toString());
//            }
//        }
//        @Override
//        public List<Cookie> loadForRequest(HttpUrl url) {
//            List<Cookie> cookiesList = cookiesMap.get(url.host());
//            return cookiesList != null ? cookiesList : new ArrayList<Cookie>();
//        }
//    };

}
