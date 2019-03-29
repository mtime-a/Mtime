package com.example.mtimeapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PCActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView mIcon;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_homepage);

//        Intent intent = new Intent();
//        user_id = intent.getStringExtra("user_id");

        initUI();

        mIcon.setOnClickListener(this);
        mUsername.setOnClickListener(this);
        back.setOnClickListener(this);

        //initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/account/i/user/info/" + user_id).build();
                    Response response = client.newCall(request).execute();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pc_homepage_back:
                finish();
                break;
            case R.id.pc_homepage_icon:
                //这里写拍照和从相册选择
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

                //从这里上传到服务器

                Toast.makeText(PCActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }
        });
        builder_username.setView(view).create().show();
    }
}
