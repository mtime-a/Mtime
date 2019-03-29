package com.example.mtimeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtimeapp.Adapter.FilmAdapter;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;

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

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mComment_num;
    private LinearLayout mComment;
    private TextView mDate;
    private LinearLayout icon_comment;
    private AlertDialog.Builder builder_text;
    private AlertDialog.Builder builder_list;
    private View view;

    private List<Map<String, Object>> list;

    private String news_id;
    private String title;
    private String body;
    private String pub_time;
    private String comment_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_news);

//        Intent intent = getIntent();
//        news_id = intent.getStringExtra("id");//这个是每条热门消息的ID

        initUI();

        mComment.setOnClickListener(this);
        icon_comment.setOnClickListener(this);

        initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/news/i/news/" + news_id).build();
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
            String status = jsonObject.getString("status");
            //注意状态
            title = jsonObject.getString("title");
            body = jsonObject.getString("body");
            pub_time = jsonObject.getString("pub_time");
            comment_num = jsonObject.getString("comment_num");
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
                mDate.setText(pub_time);
                mComment_num.setText(comment_num);
                //body还没用
            }
        });
    }

    private void initUI() {
        mTitle = findViewById(R.id.pager_news_title);
        mDate = findViewById(R.id.pager_news_date);
        icon_comment = findViewById(R.id.pager_news_write_comment);
        mComment = findViewById(R.id.pager_news_comment);
        mComment_num = findViewById(R.id.pager_news_comment_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_news_write_comment:
                initBuilder_text();
                break;
            case R.id.pager_news_comment:
//                initBuilder_list();
                Intent intent = new Intent();
                intent.setClass(NewsActivity.this, CommentsActivity.class);
                //intent.putExtra("news_id", news_id);
                startActivity(intent);
                break;
        }
    }

    private void initBuilder_list() {
        builder_list = new AlertDialog.Builder(NewsActivity.this);
        builder_list.setTitle("热门评论");
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", R.drawable.beijing);
            map.put("name", "mlj" + i);
            map.put("content", "你好" + i);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(NewsActivity.this, list, R.layout.simple_comment_list, new String[]{"icon", "name", "content"}, new int[]{R.id.comment_list_icon, R.id.comment_list_name, R.id.comment_list_content});
        builder_list.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder_list.create().show();
    }

    private void initBuilder_text() {
        builder_text = new AlertDialog.Builder(NewsActivity.this);
        builder_text.setTitle("评论");
        view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.dialog, null);
        builder_text.setPositiveButton("发表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //从这里上传到服务器

                Toast.makeText(NewsActivity.this, "发表成功", Toast.LENGTH_LONG).show();
            }
        });
        builder_text.setView(view).create().show();
    }
}
