package com.example.mtimeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

//        Intent intent=new Intent();
//        film_id=intent.getStringExtra("film_id");

        initUI();

        initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://106.13.106.1/film/i/film/" + film_id).build();
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
            title = jsonObject.getString("title");
            image = jsonObject.getString("image");
            info = jsonObject.getString("info");
            relase_date = jsonObject.getString("relase_date");
            time = jsonObject.getString("time");
            film_id = jsonObject.getString("film_id");
            mark = jsonObject.getString("mark");

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
                mDate.setText(relase_date);
                mMark.setText(mark);
                mTime.setText(time);
                Glide.with(ShowActivity.this).load(image).into(mPicture);
                //mInfo
                //body还没用
            }
        });
    }

    private void initUI() {
        mPicture = findViewById(R.id.pager_show_picture);
        mDate = findViewById(R.id.pager_show_relase_date);
        mTitle = findViewById(R.id.pager_show_title);
        mInfo = findViewById(R.id.pager_show_info);
        mMark = findViewById(R.id.pager_show_mark);
        mTime = findViewById(R.id.pager_show_time);
    }
}
