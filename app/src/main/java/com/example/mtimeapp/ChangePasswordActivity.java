package com.example.mtimeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mtimeapp.R;

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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mOld_password;
    private EditText mNew_password;
    private Button btn;
    private ImageView close;

    private String verify_id = null;
    private String waitTime = null;
    private String code = null;
    private String oldPassword = null;
    private String newPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initUI();

        close.setOnClickListener(this);
        btn.setOnClickListener(this);

        initThread();
    }
    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/i/email_verify_code").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //解析返回值
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
            //验证码id
            verify_id = jsonObject.getString("id");
            //等待时间（未解决）
            waitTime = jsonObject.getString("wait");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void postRegJsonData(){
        try {
            JSONObject body = new JSONObject();
            body.put("old_password", oldPassword);
            body.put("new_password", newPassword);
            body.put("verify_id", verify_id);
            body.put("verify_code", code);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(body));
            Request request = new Request.Builder()
                    .url("http://106.13.106.1/to_post")
                    .post(requestBody)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TAG", "获取数据失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int state;
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        state  = Integer.parseInt(jsonObject.getString("result"));
                        judgeState(state);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void judgeState(int state){
//        0:成功
//        1:原密码错误
//        2:验证码错误
//        3:未登录
//        4:未知错误
        if(state == 0 ){
            Toast.makeText(this,"修改成功",Toast.LENGTH_LONG).show();
        }
        if(state == 1 ){
            Toast.makeText(this,"原密码错误",Toast.LENGTH_LONG).show();
        }
        if(state == 2 ){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state == 3 ){
            Toast.makeText(this,"未登录",Toast.LENGTH_LONG).show();
        }
        if(state == 4 ){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }

    private void initUI() {
        close = findViewById(R.id.change_password_close);
        btn = findViewById(R.id.change_password_btn);
        mNew_password = findViewById(R.id.change_password_new);
        mOld_password = findViewById(R.id.change_password_old);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_password_close:
                finish();
                break;
            case R.id.change_password_btn:
                break;
        }
    }
}
