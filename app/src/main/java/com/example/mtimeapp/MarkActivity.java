package com.example.mtimeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarkActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mMark_1;
    private ImageView mMark_2;
    private ImageView mMark_3;
    private ImageView mMark_4;
    private ImageView mMark_5;

    private Button btn;
    private String id;
    private String session;
    private String state;
    private Integer mark;
    private String isMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isMark = intent.getStringExtra("isMark");

        SharedPreferences sp = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        session = sp.getString("cookie", "");

        initUI();

        initClick();
    }

    private void initClick() {
        mMark_1.setOnClickListener(this);
        mMark_2.setOnClickListener(this);
        mMark_3.setOnClickListener(this);
        mMark_4.setOnClickListener(this);
        mMark_5.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    private void initUI() {
        mMark_1 = findViewById(R.id.mark_1);
        mMark_2 = findViewById(R.id.mark_2);
        mMark_3 = findViewById(R.id.mark_3);
        mMark_4 = findViewById(R.id.mark_4);
        mMark_5 = findViewById(R.id.mark_5);
        btn = findViewById(R.id.mark_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mark_1:
                mMark_1.setImageResource(R.mipmap.dafenhou2);
                mMark_2.setImageResource(R.mipmap.pingfenqian);
                mMark_3.setImageResource(R.mipmap.pingfenqian);
                mMark_4.setImageResource(R.mipmap.pingfenqian);
                mMark_5.setImageResource(R.mipmap.pingfenqian);
                mark = 1;
                break;
            case R.id.mark_2:
                mMark_1.setImageResource(R.mipmap.dafenhou2);
                mMark_2.setImageResource(R.mipmap.dafenhou2);
                mMark_3.setImageResource(R.mipmap.pingfenqian);
                mMark_4.setImageResource(R.mipmap.pingfenqian);
                mMark_5.setImageResource(R.mipmap.pingfenqian);
                mark = 2;
                break;
            case R.id.mark_3:
                mMark_1.setImageResource(R.mipmap.dafenhou2);
                mMark_2.setImageResource(R.mipmap.dafenhou2);
                mMark_3.setImageResource(R.mipmap.dafenhou2);
                mMark_4.setImageResource(R.mipmap.pingfenqian);
                mMark_5.setImageResource(R.mipmap.pingfenqian);
                mark = 3;
                break;
            case R.id.mark_4:
                mMark_1.setImageResource(R.mipmap.dafenhou2);
                mMark_2.setImageResource(R.mipmap.dafenhou2);
                mMark_3.setImageResource(R.mipmap.dafenhou2);
                mMark_4.setImageResource(R.mipmap.dafenhou2);
                mMark_5.setImageResource(R.mipmap.pingfenqian);
                mark = 4;
                break;
            case R.id.mark_5:
                mMark_1.setImageResource(R.mipmap.dafenhou2);
                mMark_2.setImageResource(R.mipmap.dafenhou2);
                mMark_3.setImageResource(R.mipmap.dafenhou2);
                mMark_4.setImageResource(R.mipmap.dafenhou2);
                mMark_5.setImageResource(R.mipmap.dafenhou2);
                mark = 5;
                break;
            case R.id.mark_btn:
                if (isMark.equals("true"))
                    Toast.makeText(MarkActivity.this, "你已经评分过了", Toast.LENGTH_SHORT).show();
                else {
                    postMarkRequest();
                    finish();
                }
                break;
        }
    }

    private void postMarkRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .build();

                    Log.d("mljmljmljmlj", mark.toString());

                    FormBody formBody = new FormBody.Builder()
                            .add("id", id)
                            .add("score", mark.toString())
                            .add("session", session)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/scorePointFilm/")
                            .post(formBody)
                            .addHeader("Connection", "close")
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("TAGmlj", "获取数据失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);

                                    state = jsonObject.getString("state");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!state.equals("1"))
                                                Toast.makeText(MarkActivity.this, "评分失败", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(MarkActivity.this, "评分成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
