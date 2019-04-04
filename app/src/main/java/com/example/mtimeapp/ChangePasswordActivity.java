package com.example.mtimeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity  {
    private String verify_id = null;
    private String waitTime = null;
    private String code = null;
    private String oldPassword = null;
    private String newPassword = null;
    private String cookie;
    private String statu;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Button ChangePassword = findViewById(R.id.change_btn);
        final EditText OldPassword = findViewById(R.id.change_password);
        final EditText NewPassword = findViewById(R.id.change_Rpassword);

        SharedPreferences sps = getSharedPreferences("Cookies", Context.MODE_PRIVATE);
        cookie = sps.getString("cookie", "");
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("theName", "");

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = OldPassword.getText().toString();
                newPassword = NewPassword.getText().toString();
                initThread();
            }
        });
    }
    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build();

                FormBody formBody = new FormBody.Builder()
                        .add("oldPassword",oldPassword)
                        .add("newPassword",newPassword)
                        .add("username",username)
                        .add("session",cookie)
                        .build();

                Request request = new Request.Builder()
                        .url("http://132.232.78.106:8001/api/changePwdByself/")
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
                                JSONObject jsonObject = new JSONObject(responseData);
                                statu = jsonObject.getString("state");
                                String msg = jsonObject.getString("msg");
                                judgeChangPasswordState(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }
    private void judgeChangPasswordState(final String state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),state,Toast.LENGTH_LONG).show();
            }
        });
    }
   
}
