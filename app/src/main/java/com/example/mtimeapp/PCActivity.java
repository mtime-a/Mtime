package com.example.mtimeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;
import com.nostra13.universalimageloader.utils.L;

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

public class PCActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIcon;
    private TextView mUsername;
    private TextView mId;
    private TextView mEmil;
    private ImageView back;

    private String user_id;
    private String username;
    private String nickName;
    private String headImage;
    private String statu;
    private String email;
    private String icon;
    private String cookie;
    private AlertDialog.Builder builder_username;
    private View view;

    private AlertDialog.Builder buider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_homepage);


        SharedPreferences sps = getSharedPreferences("Cookies", Context.MODE_PRIVATE);
        cookie = sps.getString("cookie", "");
        //？？？？？？？？？？？？？？？？？？？？？？？？？
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("theName", "");
        nickName = sharedPreferences.getString("theNickname", "");
        email = sharedPreferences.getString("theEmail","");
        headImage = "http://132.232.78.106:8001/media/" + sharedPreferences.getString("theHeadImage","");

        initUI();

        mIcon.setOnClickListener(this);
        mUsername.setOnClickListener(this);
        mId.setOnClickListener(this);
        mEmil.setOnClickListener(this);
        back.setOnClickListener(this);

       // initThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsername.setText(nickName);
        mEmil.setText(email);
        mId.setText(username);
      //  Glide.with(this).load(headImage).into(mIcon);
    }
    //    private void initThread() {
//        new Thread(new Runnable() {                                                                 //新线程联网
//            @Override
//            public void run() {
//                try {                                                                                          //okHttp请求数据
//                    Log.e("TAG","子线程");
//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder().url("http://106.13.106.1/account/i/user/info/" + user_id).build();
//                    Response response = client.newCall(request).execute();
//                    Log.e("TAG", String.valueOf(response));
//                    String responseData = response.body().string();
//                    parseJSONWithJSONObject(responseData);                                                 //解析json的方法
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

//    private void parseJSONWithJSONObject(String JsonData) {               //解析JSON数据
//        try {
//            JSONObject jsonObject = new JSONObject(JsonData);
//            user_id = jsonObject.getString("user_id");
//            username = jsonObject.getString("username");
//            icon = jsonObject.getString("head");
//            email = jsonObject.getString("email");
//            Log.e("TAG",icon);
//            showResponse();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private void showResponse() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(PCActivity.this).load(icon).into(mIcon);
//                mEmil.setText(email);
//                mUsername.setText(username);
//                mId.setText(user_id);
//                //body还没用
//            }
//        });
//    }

    private void initUI() {
        mIcon = findViewById(R.id.pc_homepage_icon);
        mUsername = findViewById(R.id.pc_homepage_username);
        mId = findViewById(R.id.pc_homepage_id);
        mEmil = findViewById(R.id.pc_homepage_emil);
        back = findViewById(R.id.pc_homepage_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pc_homepage_back:
                finish();
                break;
            case R.id.pc_homepage_icon:
                //这里写拍照和从相册选择
                buider = new AlertDialog.Builder(PCActivity.this);
                final String arrItem[] = getResources().getStringArray(R.array.oem);
                buider.setItems(arrItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PCActivity.this, "你选择了第" + arrItem[which], Toast.LENGTH_LONG).show();
                    }
                });
                buider.create().show();
                break;
            case R.id.pc_homepage_username:
                initBuilder_username();
                break;
        }
    }

    private void initBuilder_username() {
        builder_username = new AlertDialog.Builder(PCActivity.this);
        builder_username.setTitle("请输入新昵称");
        view = LayoutInflater.from(PCActivity.this).inflate(R.layout.dialog, null);
        builder_username.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //***************************
                //将获取的用户名放入nickName
                //***************************
                nickName = "emmmmmmmmmm";
                changNickname(nickName);

            }
        });
        builder_username.setView(view).create().show();
    }
    private void changNickname(String nick){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("nickName",nick)
                .add("session", cookie)
                .build();

        Request request = new Request.Builder()
                .url("http://132.232.78.106:8001/api/changeNickName/")
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
                        judgeChangNicknameState(msg);
                        if(statu.equals("1")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("theNickname", nickName);
                            editor.apply();
                            mUsername.setText(nickName);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void judgeChangNicknameState(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        });
    }
}
