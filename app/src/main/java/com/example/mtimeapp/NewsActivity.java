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
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mComment_num;
    private LinearLayout mComment;
    private TextView mDate;
    private TextView mWeb;
    private TextView mAuthor;
    private TextView mLove_num;
    private LinearLayout mLove;
    private RoundImageView mPicture;
    private LinearLayout icon_comment;
    private AlertDialog.Builder builder_text;
    private View view;

    private String news_id;
    private String cookie;
    private String statu;
    private String author;
    private String photo;
    private String Title;
    private String Time;
    private String clickNum;
    private String replyNum;
    private String content;
    private ArrayList<Map<String, Object>> list_comment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_news);

        Intent intent = getIntent();
        news_id = intent.getStringExtra("id");//这个是每条热门消息的ID

        initUI();

        mComment.setOnClickListener(this);
        icon_comment.setOnClickListener(this);
        mLove.setOnClickListener(this);


        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cookie.equals("")) {
            Toast.makeText(NewsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent();
            intent1.setClass(NewsActivity.this, Log_RegActivity.class);
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
                            .add("id", news_id)
                            .add("operaType", "0")
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/getPointNews/")
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
                                    statu = jsonObject.getString("state");
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
                                                Toast.makeText(NewsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                mDate.setText(Time);
                mComment_num.setText(replyNum);
                mAuthor.setText(author);
                mLove_num.setText(clickNum);
                Glide.with(NewsActivity.this).load("http://132.232.78.106:8001/media/" + photo).into(mPicture);
                new RichText(NewsActivity.this, mWeb, content);
            }
        });
    }

    private void initUI() {
        mLove = findViewById(R.id.pager_news_love);
        mLove_num = findViewById(R.id.pager_news_love_num);
        mAuthor = findViewById(R.id.pager_news_author);
        mTitle = findViewById(R.id.pager_news_title);
        mDate = findViewById(R.id.pager_news_date);
        icon_comment = findViewById(R.id.pager_news_write_comment);
        mComment = findViewById(R.id.pager_news_comment);
        mComment_num = findViewById(R.id.pager_news_comment_num);
        mPicture = findViewById(R.id.pager_news_picture);
        mWeb = findViewById(R.id.pager_news_web);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_news_write_comment:
                initBuilder_text();
                break;
            case R.id.pager_news_comment:
                Intent intent = new Intent();
                intent.setClass(NewsActivity.this, CommentsActivity.class);
                intent.putExtra("id", news_id);
                intent.putExtra("type", "news");
                startActivity(intent);
                break;
            case R.id.pager_news_love:
                //写点赞的逻辑
                break;
        }
    }


    private void initBuilder_text() {
        builder_text = new AlertDialog.Builder(NewsActivity.this);
        builder_text.setTitle("评论");
        view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.dialog, null);
        final EditText editText = view.findViewById(R.id.text);
        builder_text.setPositiveButton("发表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = editText.getText().toString();
                postReplyRequest_short(content);
            }
        });
        builder_text.setView(view).create().show();
    }

    private void postReplyRequest_short(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", news_id)
                            .add("content", content)
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/replyPointNews/")
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
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    String statu = jsonObject.getString("state");
                                    if (statu.equals("1")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(NewsActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(NewsActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
