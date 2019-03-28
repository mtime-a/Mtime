package com.example.mtimeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FilmActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundImageView mPicture;
    private TextView mTitle;
    private CircleImageView mIcon;
    private TextView mName;
    private LinearLayout icon_comment;
    private LinearLayout comment;
    private AlertDialog.Builder builder_text;
    private AlertDialog.Builder builder_list;
    private View view;
    private List<Map<String, Object>> list;
    private String film_id;
    private String title;
    private String subtitle;
    private String body;
    private String author_name;
    private String author_head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_film);

//        Intent intent=new Intent();
//        film_id=intent.getStringExtra("film_id");

        initUI();

        initThread();

        initClick();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/film/i/film_review/" + film_id).build();
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

            title = jsonObject.getString("title");
            subtitle = jsonObject.getString("subtitle");
            body = jsonObject.getString("body");
            author_name = jsonObject.getString("author_name");
            author_head = jsonObject.getString("author_head");

            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTitle.setText(title);
                mName.setText(author_name);
                Glide.with(FilmActivity.this).load(author_head).into(mIcon);
                //body还没用 subtitle也没用
            }
        });
    }

    private void initClick() {
        icon_comment.setOnClickListener(this);
        comment.setOnClickListener(this);
    }

    private void initUI() {
        mPicture = findViewById(R.id.pager_film_picture);
        mTitle = findViewById(R.id.pager_film_title);
        mIcon = findViewById(R.id.pager_film_icon);
        mName = findViewById(R.id.pager_film_name);
        icon_comment = findViewById(R.id.pager_film_write_comment);
        comment = findViewById(R.id.pager_film_comment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_film_write_comment:
                initBuilder_text();
                break;
            case R.id.pager_film_comment:
                //initBuilder_list();
                Intent intent = new Intent();
                intent.setClass(FilmActivity.this, CommentsActivity.class);
                intent.putExtra("film_id", film_id);
                break;
        }
    }

    //显示评论dialog
    private void initBuilder_list() {
        builder_list = new AlertDialog.Builder(FilmActivity.this);
        builder_list.setTitle("热门评论");
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", R.drawable.beijing);
            map.put("name", "mlj" + i);
            map.put("content", "你好" + i);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(FilmActivity.this, list, R.layout.simple_comment_list, new String[]{"icon", "name", "content"}, new int[]{R.id.comment_list_icon, R.id.comment_list_name, R.id.comment_list_content});
        builder_list.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder_list.create().show();
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
}