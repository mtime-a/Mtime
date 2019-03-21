package com.example.mtimeapp;

import android.content.DialogInterface;
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

import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundImageView picture;
    private TextView title;
    private CircleImageView icon;
    private TextView name;
    private LinearLayout icon_comment;
    private ImageView comment;
    private ImageView love;
    private AlertDialog.Builder builder_text;
    private AlertDialog.Builder builder_list;
    private View view;
    private List<Map<String, Object>> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_news);

        initUI();

        initNews();
    }

    private void initNews() {
        icon_comment.setOnClickListener(this);
        comment.setOnClickListener(this);
        love.setOnClickListener(this);
    }

    private void initUI() {
        picture = findViewById(R.id.pager_news_picture);
        title = findViewById(R.id.pager_news_title);
        icon = findViewById(R.id.pager_news_icon);
        name = findViewById(R.id.pager_news_name);
        icon_comment = findViewById(R.id.pager_news_write_comment);
        comment = findViewById(R.id.pager_news_comment);
        love = findViewById(R.id.pager_news_love);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_news_write_comment:
                initBuilder_text();
                break;
            case R.id.pager_news_comment:
                initBuilder_list();
                break;
            case R.id.pager_news_love:
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
                Toast.makeText(NewsActivity.this, "发表成功", Toast.LENGTH_LONG).show();
            }
        });
        builder_text.setView(view).create().show();
    }
}
