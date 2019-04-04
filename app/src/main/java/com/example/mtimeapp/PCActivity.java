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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String email;
    private String icon;
    private AlertDialog.Builder builder_username;
    private View view;

    private AlertDialog.Builder buider;
    private String headImage;
    private String nickName;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_homepage);

//        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
//        user_id = sps.getString("theName", "");

        initUI();

        //必须是登录完跳转过来的时候才能用这个
//        bundle = new Bundle();
//        Intent intent = getIntent();
//        bundle = intent.getBundleExtra("UserMessage");
//
//        username = bundle.getString("username");
//        nickName = bundle.getString("nickName");
//        headImage = bundle.getString("headImage");
//
//        mUsername.setText(nickName);
//        mId.setText(username);

        mIcon.setOnClickListener(this);
        mUsername.setOnClickListener(this);
        back.setOnClickListener(this);

        // initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    Log.e("TAG", "子线程");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://106.13.106.1/account/i/user/info/" + user_id).build();
                    Response response = client.newCall(request).execute();
                    Log.e("TAG", String.valueOf(response));
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);                                                 //解析json的方法
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String JsonData) {               //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            user_id = jsonObject.getString("user_id");
            username = jsonObject.getString("username");
            icon = jsonObject.getString("head");
            email = jsonObject.getString("email");
            Log.e("TAG", icon);
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(PCActivity.this).load(icon).into(mIcon);
                mEmil.setText(email);
                mUsername.setText(username);
                mId.setText(user_id);
                //body还没用
            }
        });
    }

    private void initUI() {
        mIcon = findViewById(R.id.pc_homepage_icon);
        mUsername = findViewById(R.id.pc_homepage_username);
        mId = findViewById(R.id.pc_homepage_id);
        mEmil = findViewById(R.id.pc_homepage_emil);
        back = findViewById(R.id.pc_homepage_back);
    }

    //拍照修改头像
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

    //修改个人信息的昵称
    private void initBuilder_username() {
        builder_username = new AlertDialog.Builder(PCActivity.this);
        builder_username.setTitle("请输入新昵称");
        view = LayoutInflater.from(PCActivity.this).inflate(R.layout.dialog, null);
        builder_username.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //从这里上传到服务器

                Toast.makeText(PCActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }
        });
        builder_username.setView(view).create().show();
    }


}
