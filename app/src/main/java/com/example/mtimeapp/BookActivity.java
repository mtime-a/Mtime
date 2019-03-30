package com.example.mtimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookActivity extends AppCompatActivity {

    private RoundImageView mPicture;
    private TextView mTitle;
    private TextView mTime;
    private TextView mDate;
    private TextView mInfo;
    private TextView mMark;

    private String title;
    private String image;
    private String info;
    private String relase_date;
    private String time;
    private String film_id;
    private String mark;
    private String marked_members;
    private String comment_members;
    private TextView mComments_num;
    private TextView mMark_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();
        film_id = intent.getStringExtra("film_id");

        initUI();

        initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://39.96.208.176/film/i/film/?film_id=" + film_id).build();
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
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            //注意状态
            if (status.equals("ok")) {
                title = jsonObject.getString("title");
                image = jsonObject.getString("image");
                //info = jsonObject.getString("info");
                relase_date = jsonObject.getString("relase_date");
                time = jsonObject.getString("time");
                film_id = jsonObject.getString("film_id");
                mark = jsonObject.getString("mark");
                marked_members = jsonObject.getString("marked_members");
                comment_members = jsonObject.getString("comment_members");

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
                mTitle.setText(title);
                mDate.setText(relase_date);
                mMark.setText(mark);
                mTime.setText(time);
                mComments_num.setText(comment_members);
                mMark_num.setText(marked_members);
                Glide.with(BookActivity.this).load("http://39.96.208.176" + image).into(mPicture);
                //mInfo
                //body还没用
            }
        });
    }

    private void initUI() {
        mPicture = findViewById(R.id.pager_book_picture);
        mDate = findViewById(R.id.pager_book_relase_date);
        mTitle = findViewById(R.id.pager_book_title);
        mInfo = findViewById(R.id.pager_book_info);
        mMark = findViewById(R.id.pager_book_mark);
        mTime = findViewById(R.id.pager_book_time);
        mMark_num = findViewById(R.id.pager_book_marked_members);
        mComments_num = findViewById(R.id.pager_book_commented_members);
    }
}
