package com.example.mtimeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mtimeapp.Adapter.MyCommentsAdapter;

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

public class MyCommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView_news;
    private RecyclerView recyclerView_film;
    private LinearLayout comments_film;//同下
    private LinearLayout comments_news;//当没有新闻评论的时候就隐藏
    private LinearLayout comments_1;//这个是当内容的时候就显示这个
    private LinearLayout comments_0;//没有内容的时候就显示这个
    private List<Map<String, Object>> list_news;
    private List<Map<String, Object>> list_film;
    private String cookie;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomments);

        initUI();

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");

        postSession("http://132.232.78.106:8001/api/getMyFirmComment/",i = 0);
        postSession("http://132.232.78.106:8001/api/getMyNewsComment/",i = 1);

    }

    private void postSession(String url, final int i) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("session", cookie)
                .build();

        Request request = new Request.Builder()
                .addHeader("Connection","close")
                .url(url)
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
                    if(i == 0){
                        parseJSONWithFilmJSONObject(responseData);
                    }
                    if(i == 1){
                        parseJSONWithNewsJSONObject(responseData);
                    }
                }
            }
        });
    }
    private void parseJSONWithFilmJSONObject(String JsonData){
        Log.e("MyCommentFilm",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            String state = jsonObject.getString("state");
            if(state.equals("1")){
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.getString("id");
                String content = jsonObject1.getString("content");
                String filmId = jsonObject1.getString("firmId");
                String filmName = jsonObject1.getString("firmName");
                String Time = jsonObject1.getString("Time");
                list_film = new ArrayList<>();
                Map map = new HashMap();
                map.put("type","film");
                map.put("id", id);
                map.put("content",content);
                map.put("filmId",filmId );
                map.put("Time", Time);
                map.put("filmName", filmName);
                list_film.add(map);
                showFilmResponse();
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseJSONWithNewsJSONObject(String JsonData){
        Log.e("MyCommentNews",JsonData);
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            String state = jsonObject.getString("state");
            if(state.equals("1")){
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.getString("id");
                String content = jsonObject1.getString("content");
                String newsId = jsonObject1.getString("newsId");
                String newsTitle = jsonObject1.getString("newsTitle");
                String create_time = jsonObject1.getString("create_time");
                list_news = new ArrayList<>();
                Map map = new HashMap();
                map.put("type","news");
                map.put("id", id);
                map.put("content",content);
                map.put("newsId",newsId );
                map.put("newsTitle", newsTitle);
                map.put("create_time", create_time);
                list_news.add(map);
                showNewsResponse();
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showFilmResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager_film = new LinearLayoutManager(MyCommentsActivity.this);
                recyclerView_film.setLayoutManager(manager_film);
                MyCommentsAdapter adapter_film = new MyCommentsAdapter(MyCommentsActivity.this, list_film);
                recyclerView_film.setAdapter(adapter_film);
            }
        });
    }
    private void showNewsResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
