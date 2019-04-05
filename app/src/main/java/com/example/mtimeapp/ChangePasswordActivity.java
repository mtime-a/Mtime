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
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String reNewPassword = null;
    private String cookie;
    private String statu;
    private String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Button ChangePassword = findViewById(R.id.change_btn);
        ImageView close = findViewById(R.id.close);
        final EditText OldPassword = findViewById(R.id.change_old_password);
        final EditText NewPassword = findViewById(R.id.change_new_password);
        final EditText ReNewPassword = findViewById(R.id.change_Rnew_password);

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("theName", "");
        cookie = sharedPreferences.getString("cookie","");

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = OldPassword.getText().toString();
                newPassword = NewPassword.getText().toString();
                reNewPassword = ReNewPassword.getText().toString();
                Log.e("changepassword",oldPassword + "and" + newPassword);
                if(oldPassword.equals("")||oldPassword == null){
                    Toast.makeText(getApplicationContext(),"请填入密码",Toast.LENGTH_LONG).show();
                }else {
                    if(reNewPassword.equals(newPassword)){
                        boolean judge = checkCode(newPassword);
                        if(judge){
                            changePassword();
                        }else {
                            Toast.makeText(getApplicationContext(),"请填入正确的密码格式",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void changePassword() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("oldPassword",oldPassword)
                .add("newPassword",newPassword)
                .add("username",username)
                .add("session",cookie)
                .build();

        Log.e("changePassword",oldPassword + newPassword + username + cookie );
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
                Log.e("ChangePassword",responseData);
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        statu = jsonObject.getString("statu");
                        String msg = jsonObject.getString("msg");
                        judgeChangPasswordState(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void judgeChangPasswordState(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        });
        if(statu.equals("1")){
            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie","");
            editor.putString("theName", "");
            editor.putString("theNickname", "");
            editor.putString("theHeadImage","");
            editor.putString("theEmail","");
            editor.apply();
            Intent intent = new Intent();
            intent.setClass(this, Log_RegActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean checkCode(String code) {
        //限制密码
        Pattern pattern = Pattern.compile("[\\S]{8,16}$");
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }
   
}
