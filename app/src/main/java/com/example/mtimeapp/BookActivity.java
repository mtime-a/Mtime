package com.example.mtimeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.RoundImageView;

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

public class BookActivity extends AppCompatActivity {

    private RoundImageView mPicture;
    private TextView mTitle;
    private TextView mDate;
    private TextView mMark;
    private TextView mMark_num;
    private LinearLayout mComments;
    private TextView mComments_num;

    private String title;
    private String image;
    private String relase_date;
    private String film_id;
    private String mark;
    private String marked_members;
    private String comment_members;
    private String cookie;
    private String id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();
        film_id = intent.getStringExtra("film_id");

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");

        initUI();

        if (cookie.equals("")) {
            Toast.makeText(BookActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent();
            intent1.setClass(BookActivity.this, Log_RegActivity.class);
            startActivity(intent1);
            finish();
        } else
            initData();//加载详情的内容
    }

    private void initData() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", film_id)
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/getPointFilm/")
                            .post(formBody)
                            .addHeader("Connection", "close")
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("TAG", "获取数据失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("mlj", responseData);
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    String statu = jsonObject.getString("state");
                                    if (statu.equals("1")) {

                                        JSONObject jsonObject_result = jsonObject.getJSONObject("result");
                                        id = jsonObject_result.getString("id");
                                        title = jsonObject_result.getString("title");
                                        image = jsonObject_result.getString("image");
                                        mark = jsonObject_result.getString("mark");
                                        relase_date = jsonObject_result.getString("relase_date");
                                        marked_members = jsonObject_result.getString("marked_members");
                                        comment_members = jsonObject_result.getString("comment_members");

                                        showResponse();
                                    } else {
                                        final String msg = jsonObject.getString("msg");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
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

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTitle.setText(title);
                mDate.setText(relase_date);
                mMark.setText(mark);
                mComments_num.setText(comment_members);
                mMark_num.setText(marked_members);
                Glide.with(BookActivity.this).load("http://132.232.78.106:8001" + image).into(mPicture);
                mComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(BookActivity.this, CommentsActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void initUI() {
        mPicture = findViewById(R.id.pager_book_picture);
        mDate = findViewById(R.id.pager_book_relase_date);
        mTitle = findViewById(R.id.pager_book_title);
        mMark = findViewById(R.id.pager_book_mark);
        mMark_num = findViewById(R.id.pager_book_mark_num);
        mComments_num = findViewById(R.id.pager_book_comment_num);
        mComments = findViewById(R.id.pager_book_comment);
    }
}
