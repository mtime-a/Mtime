package com.example.mtimeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundImageView mPicture;
    private TextView mTitle;
    private TextView mDate;
    private TextView mMark;
    private LinearLayout mWriteComments;
    private LinearLayout mComments;
    private LinearLayout mLove;
    private TextView mComments_num;
    private TextView mLove_num;
    private AlertDialog.Builder builder_text;
    private View view;

    private String title;
    private String image;
    private String relase_date;
    private String film_id;
    private String mark;
    private String marked_members;
    private String comment_members;
    private String cookie;
    private String id;
    private boolean flag;
    private boolean Flag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initUI();

        Intent intent = getIntent();
        film_id = intent.getStringExtra("film_id");

        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");

        initClick();

        if (cookie.equals("")) {
            Toast.makeText(BookActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent();
            intent1.setClass(BookActivity.this, Log_RegActivity.class);
            startActivity(intent1);
            finish();
        } else
            initData();//加载详情的内容
    }

    private void initClick() {
        mLove.setOnClickListener(this);
        mComments.setOnClickListener(this);
        mWriteComments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_book_love:
                //写点赞的事件
                break;
            case R.id.pager_book_comment:
                Intent intent = new Intent();
                intent.setClass(BookActivity.this, CommentsActivity.class);
                intent.putExtra("id", film_id);
                intent.putExtra("type", "film");
                startActivity(intent);
                break;
            case R.id.pager_book_write_comment:
                initBuilder_text();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cookie.equals("")) {
            Toast.makeText(BookActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent();
            intent1.setClass(BookActivity.this, Log_RegActivity.class);
            startActivity(intent1);
            finish();
        } else
            initData();//加载详情的内容
    }

    private void initData() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", film_id)
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/getPointFilm/")
                            .post(formBody)
                            .addHeader("Connection", "close")
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("TAG", "获取数据失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("mlj", responseData);
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    String statu = jsonObject.getString("state");
                                    if (statu.equals("1")) {

                                        JSONObject jsonObject_result = jsonObject.getJSONObject("result");
                                        id = jsonObject_result.getString("id");
                                        title = jsonObject_result.getString("title");
                                        image = jsonObject_result.getString("image");
                                        mark = jsonObject_result.getString("mark");
                                        relase_date = jsonObject_result.getString("relase_date");
                                        marked_members = jsonObject_result.getString("marked_members");
                                        comment_members = jsonObject_result.getString("comment_members");

                                        showResponse();
                                    } else {
                                        final String msg = jsonObject.getString("msg");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTitle.setText(title);
                mDate.setText(relase_date);
                mMark.setText(mark);
                mComments_num.setText(comment_members);
                mLove_num.setText(marked_members);
                Glide.with(BookActivity.this).load("http://132.232.78.106:8001" + image).into(mPicture);
            }
        });
    }

    private void initUI() {
        mWriteComments = findViewById(R.id.pager_book_write_comment);
        mPicture = findViewById(R.id.pager_book_picture);
        mDate = findViewById(R.id.pager_book_relase_date);
        mTitle = findViewById(R.id.pager_book_title);
        mMark = findViewById(R.id.pager_book_mark);
        mComments_num = findViewById(R.id.pager_book_comment_num);
        mComments = findViewById(R.id.pager_book_comment);
        mLove = findViewById(R.id.pager_book_love);
        mLove_num = findViewById(R.id.pager_book_love_num);
    }


    private void initBuilder_text() {
        builder_text = new AlertDialog.Builder(BookActivity.this);
        view = LayoutInflater.from(BookActivity.this).inflate(R.layout.dialog_change, null);
        final EditText editText_content = view.findViewById(R.id.dialog_change_text);
        final EditText editText_long_title = view.findViewById(R.id.dialog_change_long_title);
        final EditText editText_long_subtitle = view.findViewById(R.id.dialog_change_long_subtitle);
        final LinearLayout dialog_long = view.findViewById(R.id.dialog_change_long);
        final TextView change_text = view.findViewById(R.id.dialog_change_change_text);
        final LinearLayout change = view.findViewById(R.id.dialog_change_change);

        dialog_long.setVisibility(View.GONE);

        change.setTag(false);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当点击时，首先判断是否已经点击过
                flag = (boolean) change.getTag();
                if (!flag) {
                    //没有被点击过
                    dialog_long.setVisibility(View.GONE);
                    change_text.setText("短评论");
                    change.setTag(true);
                    Flag = false;
                } else {
                    //已经点击过了
                    dialog_long.setVisibility(View.VISIBLE);
                    change_text.setText("长评论");
                    change.setTag(false);
                    Flag = true;//被点击了
                }
            }
        });

        builder_text.setPositiveButton("发表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contents = editText_content.getText().toString();
                //点击后切换成长影评Flay返回true则为长影评反之为短影评
                if (Flag) {
                    String title = editText_long_title.getText().toString();
                    String subtitle = editText_long_subtitle.getText().toString();
                    postReplyRequest_long(title, subtitle, contents);
                } else
                    postReplyRequest_short(contents);
                //从这里上传到服务器
            }
        });
        builder_text.setView(view).create().show();
    }

    private void postReplyRequest_short(final String contents) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", film_id)
                            .add("content", contents)
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/replyPointFilm/")
                            .post(formBody)
                            .addHeader("Connection", "close")
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
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    String statu = jsonObject.getString("state");

                                    Log.d("mlj", "状态为" + statu);
                                    if (statu.equals("1")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postReplyRequest_long(final String title, final String subtitle, final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    FormBody formBody = new FormBody.Builder()
                            .add("id", film_id)
                            .add("title", title)
                            .add("subtitle", subtitle)
                            .add("content", content)
                            .add("thumbnail", image)//写死
                            .add("session", cookie)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/reviewPointFilm/")
                            .post(formBody)
                            .addHeader("Connection", "close")
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
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    String statu = jsonObject.getString("state");
                                    if (statu.equals("1")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
