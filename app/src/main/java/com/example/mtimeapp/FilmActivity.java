package com.example.mtimeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.RoundImageView;
import com.example.mtimeapp.Util.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FilmActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundImageView mPicture;
    private RoundImageView mAuthor_icon;
    private TextView mDate;
    private TextView mWeb;
    private TextView mTitle;
    private LinearLayout mLove;
    private TextView mLove_num;
    private TextView mName;
    private LinearLayout icon_comment;
    private LinearLayout mComment;
    private TextView mComment_num;
    private AlertDialog.Builder builder_text;
    private View view;

    private String film_id;
    private String cookie;
    private String author;
    private String photo;
    private String Title;
    private String Time;
    private String clickNum;
    private String replyNum;
    private String content;
    private ArrayList<Map<String, Object>> list_comment;
    private String picture;
    private String author_head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_film);

        Intent intent = getIntent();
        film_id = intent.getStringExtra("comment_id");
        picture = intent.getStringExtra("picture");
        author_head = intent.getStringExtra("author_head");
        Log.d("mljmljmlj", picture);
        initUI();

        icon_comment.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mLove.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        if (cookie.equals("")) {
            Toast.makeText(FilmActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent();
            intent1.setClass(FilmActivity.this, Log_RegActivity.class);
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
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", film_id)
                            //.add("operaType", "0")
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/getPointFilmReview/")
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
                                        author = jsonObject_result.getString("author");
                                        photo = jsonObject_result.getString("photo");
                                        Title = jsonObject_result.getString("Title");
                                        Time = jsonObject_result.getString("Time");
                                        clickNum = jsonObject_result.getString("clickNum");
                                        replyNum = jsonObject_result.getString("replyNum");
                                        content = jsonObject_result.getString("content");

                                        showResponse();
                                    } else {
                                        final String msg = jsonObject.getString("msg");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(FilmActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                mTitle.setText(Title);
                mName.setText(author);
                mComment_num.setText(replyNum);
                mLove_num.setText(clickNum);
                mDate.setText(Time);
                if (photo.equals("None"))
                    Glide.with(FilmActivity.this).load(picture).into(mPicture);
                else
                    Glide.with(FilmActivity.this).load("http://132.232.78.106:8001/media/" + photo).into(mPicture);
                Glide.with(FilmActivity.this).load(author_head).into(mAuthor_icon);
                new RichText(FilmActivity.this, mWeb, content);
            }
        });
    }


    private void initUI() {
        mAuthor_icon = findViewById(R.id.pager_film_icon);
        mLove = findViewById(R.id.pager_film_love);
        mLove_num = findViewById(R.id.pager_film_love_num);
        mDate = findViewById(R.id.pager_film_date);
        mWeb = findViewById(R.id.pager_film_web);
        mPicture = findViewById(R.id.pager_film_picture);
        mTitle = findViewById(R.id.pager_film_title);
        mName = findViewById(R.id.pager_film_name);
        icon_comment = findViewById(R.id.pager_film_write_comment);
        mComment = findViewById(R.id.pager_film_comment);
        mComment_num = findViewById(R.id.pager_film_comment_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_film_write_comment:
                initBuilder_text();
                break;
            case R.id.pager_film_comment:
                Intent intent = new Intent();
                intent.setClass(FilmActivity.this, CommentsActivity.class);
                intent.putExtra("id", film_id);
                intent.putExtra("type", "film");
                startActivity(intent);
                break;
            case R.id.pager_film_love:
                //点赞操作还没写
                break;
        }
    }

    private void initBuilder_text() {
        builder_text = new AlertDialog.Builder(FilmActivity.this);
        builder_text.setTitle("评论");
        view = LayoutInflater.from(FilmActivity.this).inflate(R.layout.dialog, null);
        builder_text.setPositiveButton("发表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FilmActivity.this, "发表成功", Toast.LENGTH_LONG).show();

                //从这里上传到服务器
            }
        });
        builder_text.setView(view).create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}