package com.example.mtimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mtimeapp.Adapter.CommentsAdapter;
import com.example.mtimeapp.Adapter.FilmAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentsActivity extends AppCompatActivity {

    private String news_id;
    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        news_id = intent.getStringExtra("news_id");

        recyclerView = findViewById(R.id.comments_recyclerview);

        initThread();

//        list=new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            Map map = new HashMap();
//            map.put("author_name", "mlj" + i);
//            list.add(map);
//        }
//
//        LinearLayoutManager manager = new LinearLayoutManager(CommentsActivity.this);
//        recyclerView.setLayoutManager(manager);
//        CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this, list);
//        recyclerView.setAdapter(adapter);
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://39.96.208.176/news/i/comment_list/?news_id=" + news_id).build();
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
            list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(JsonData);
            //String status = jsonObject.getString("status");
            //注意状态
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String content = jsonObject1.getString("content");
                String author_id = jsonObject1.getString("author_id");
                String author_name = jsonObject1.getString("author_name");
                String author_head = jsonObject1.getString("author_head");
                String time = jsonObject1.getString("time");

                Map map = new HashMap();
                map.put("content", content);
                map.put("author_id", author_id);
                map.put("author_name", author_name);
                map.put("author_head", author_head);
                map.put("time", time);

                list.add(map);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(CommentsActivity.this);
                recyclerView.setLayoutManager(manager);
                CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this, list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
