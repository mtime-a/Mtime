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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mSend;
    private EditText mAccount;
    private EditText mEmil;
    private EditText mCode;
    private EditText mPassword;
    private Button btn_push;
    private ImageView close;
    private String email;
    private String account;
    private String password;
    private String code;
    private String statu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        initUI();

        initClick();

    }

    private void initClick() {
        mSend.setOnClickListener(this);
        btn_push.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_password_close:
                finish();
                break;
            case R.id.find_password_send:
                //进行发送验证码请求
                account = mAccount.getText().toString();
                email = mEmil.getText().toString();
                RequestCode();
                break;
            case R.id.find_password_btn_push:
                //判断是否数据合理
                //下面判断是不是能修改
                password = mPassword.getText().toString();
                code = mCode.getText().toString();
                account = mAccount.getText().toString();
                postChangePassword();
                break;
        }
    }

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
                            .add("username",account)
                            .add("email", email)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/LookForPwd/")
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject_Code(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                      RequestCode();
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
                Toast.makeText(FindPasswordActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initUI() {
        mSend = findViewById(R.id.find_password_send);
        mAccount = findViewById(R.id.find_password_account);
        mEmil = findViewById(R.id.find_password_mail);
        mCode = findViewById(R.id.find_password_code);
        mPassword = findViewById(R.id.find_password_password);
        btn_push = findViewById(R.id.find_password_btn_push);
        close = findViewById(R.id.find_password_close);
    }
    private void postChangePassword() {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("username", account)
                .add("newPwd",password)
                .add("code", code)
                .build();

        Log.e("FindPassword",code);
        Request request = new Request.Builder()
                .url("http://132.232.78.106:8001/api/changePwd/")//请求的url
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
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        statu = jsonObject.getString("statu");
                        String msg = jsonObject.getString("msg");
                        judgeState(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
    private void judgeState(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                if(statu.equals(1)){
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),Log_RegActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
