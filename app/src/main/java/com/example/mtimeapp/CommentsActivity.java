package com.example.mtimeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.Adapter.CommentsAdapter;
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

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String cookie;
    private String statu;
    private ArrayList<Map<String, Object>> list_comment;
    private String id;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");//电影id或者新闻id

        recyclerView = findViewById(R.id.comments_recyclerview);

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");

        if (cookie.equals("")) {
            Toast.makeText(CommentsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent();
            intent1.setClass(CommentsActivity.this, Log_RegActivity.class);
            startActivity(intent1);
        } else {
            if (type.equals("news"))
                initData("http://132.232.78.106:8001/api/getPointNews/");//加载评论的内容
            else if (type.equals("film"))
                initData("http://132.232.78.106:8001/api/getPointFilm/");
            else
                Toast.makeText(CommentsActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
        }

    }

    private void initData(final String url) {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .writeTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", id)
                            //.add("operaType", "0")
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .addHeader("Connection", "close")
                            .build();

                    Log.d("mlj", "request" + request.toString());

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException Ioexception) {
                            Log.e("TAG", "获取数据失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.isSuccessful()) {
                                String responseData = response.body().string();

                                Log.d("mlj", "dasffadsfasd" + responseData);

                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    statu = jsonObject.getString("state");
                                    if (statu.equals("1")) {
                                        JSONObject jsonObject_result = jsonObject.getJSONObject("result");
                                        list_comment = new ArrayList<>();
                                        JSONArray jsonArray_comment = jsonObject_result.getJSONArray("replys");

                                        for (int i = 0; i < jsonArray_comment.length(); i++) {
                                            JSONObject jsonObject_comment = jsonArray_comment.getJSONObject(i);

                                            String id = jsonObject_comment.getString("id");
                                            String author = jsonObject_comment.getString("author");
                                            String autherHeadPhoto = jsonObject_comment.getString("autherHeadPhoto");
                                            String Time = jsonObject_comment.getString("Time");
                                            String content = jsonObject_comment.getString("content");

                                            Map<String, Object> map = new HashMap();

                                            map.put("id", id);
                                            map.put("author", author);
                                            map.put("autherHeadPhoto", "http://132.232.78.106:8001/media/" + autherHeadPhoto);
                                            map.put("Time", Time);
                                            map.put("content", content);
                                            if(type.equals("news")){
                                                map.put("type","news");
                                            }else if(type.equals("film")){
                                                map.put("type","film");
                                            }

                                            list_comment.add(map);
                                        }
                                        showResponse();
                                    } else {
                                        final String msg = jsonObject.getString("msg");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(CommentsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException jsonexception) {
                                    jsonexception.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("mlj", "mlj=2");
                }
            }
        }).start();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(CommentsActivity.this);
                recyclerView.setLayoutManager(manager);
                CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this, list_comment);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
