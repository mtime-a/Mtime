package com.example.mtimeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.mtimeapp.Util.RichText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

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
    private TextView mMark_num;
    private TextView mComments_num;

    private String title;
    private String image;
    private String info;
    private String relase_date;
    private String time;
    private String film_id;
    private String mark;
    private String marked_members;
    private String comment_members;
    private String cookie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        film_id = intent.getStringExtra("film_id");

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");

        initUI();

        initClick();

        initDate();
    }

    private void initClick() {

    }

    private void initDate() {
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
                Glide.with(ShowActivity.this).load("http://39.96.208.176" + image).into(mPicture);
                //new RichText(ShowActivity.this, mInfo, info);
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
        mMark_num = findViewById(R.id.pager_show_marked_members);
        mComments_num = findViewById(R.id.pager_show_commented_members);
    }



}
