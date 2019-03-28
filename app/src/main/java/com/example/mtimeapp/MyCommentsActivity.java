package com.example.mtimeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mtimeapp.Adapter.MyCommentsAdapter;

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

public class MyCommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView_news;
    private RecyclerView recyclerView_film;
    private LinearLayout comments_film;//同下
    private LinearLayout comments_news;//当没有新闻评论的时候就隐藏
    private LinearLayout comments_1;//这个是当内容的时候就显示这个
    private LinearLayout comments_0;//没有内容的时候就显示这个
    private List<Map<String, Object>> list_news;
    private List<Map<String, Object>> list_film;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomments);

        initUI();

        //initThread();

    }

    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/account/i/user/comments/<user_id>").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String JsonData) {
        try {
            list_news = new ArrayList<>();
            list_film = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            if (status.equals("ok")) {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Integer type = Integer.parseInt(jsonObject1.getString("type"));
                    String content = jsonObject1.getString("content");
                    String title = jsonObject1.getString("title");
                    String image = jsonObject1.getString("image");
                    String id = jsonObject1.getString("id");

                    Map map = new HashMap();

                    if (type == 0) {
                        map.put("content", content);
                        map.put("title", title);
                        map.put("image", image);
                        map.put("id", id);
                        list_news.add(map);
                    } else {
                        map.put("title", title);
                        map.put("image", image);
                        map.put("id", id);
                        map.put("content", content);
                        list_film.add(map);
                    }
                }
                showResponse();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (list_film.size() == 0 && list_news.size() == 0) {
                    comments_news.setVisibility(View.GONE);
                    comments_film.setVisibility(View.GONE);
                } else {
                    if (list_film.size() == 0)
                        comments_film.setVisibility(View.GONE);
                    else if (list_news.size() == 0)
                        comments_news.setVisibility(View.GONE);
                }

                LinearLayoutManager manager_film = new LinearLayoutManager(MyCommentsActivity.this);
                recyclerView_film.setLayoutManager(manager_film);
                MyCommentsAdapter adapter_film = new MyCommentsAdapter(MyCommentsActivity.this, list_film);
                recyclerView_film.setAdapter(adapter_film);

                LinearLayoutManager manager_news = new LinearLayoutManager(MyCommentsActivity.this);
                recyclerView_news.setLayoutManager(manager_news);
                MyCommentsAdapter adapter_news = new MyCommentsAdapter(MyCommentsActivity.this, list_news);
                recyclerView_news.setAdapter(adapter_news);
            }
        });
    }

    private void initUI() {
        recyclerView_news = findViewById(R.id.mycomments_recyclerview_news);
        recyclerView_film = findViewById(R.id.mycomments_recyclerview_film);
        comments_film = findViewById(R.id.mycomments_film);
        comments_news = findViewById(R.id.mycomments_news);
        comments_1 = findViewById(R.id.mycomments_1);
        //comments_0 = findViewById(R.id.mycomments_0);
    }
}
