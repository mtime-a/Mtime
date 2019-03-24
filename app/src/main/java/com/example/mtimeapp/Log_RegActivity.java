package com.example.mtimeapp;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Log_RegActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout reg;
    private EditText reg_account;
    private EditText reg_password;
    private EditText reg_mail;
    private EditText reg_code;
    private Button reg_btn;
    private Button reg_get_verify_code;
    private TextView reg_switch;
    private TextView reg_find_password;

    private LinearLayout log;
    private EditText log_account;
    private EditText log_password;
    private Button log_btn;
    private TextView log_switch;
    private TextView log_find_password;

    private ImageView close;

    private String password;
    private String account;
    private String verify_id = null;
    private String UserId;
    private String email;
    private String code;
    private String name;
    private String waitTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        initUI();

        reg.setVisibility(View.GONE);

        close.setOnClickListener(this);

        initLog();

        initReg();
    }

    private void initReg() {
        reg_switch.setOnClickListener(this);
        reg_find_password.setOnClickListener(this);
        reg_btn.setOnClickListener(this);
    }


    private void initLog() {
        log_switch.setOnClickListener(this);
        log_find_password.setOnClickListener(this);
        log_btn.setOnClickListener(this);
    }

    private void initUI() {

        close = findViewById(R.id.close);
        //下面是关于reg的监听
        reg = findViewById(R.id.reg);
        reg_account = findViewById(R.id.reg_account);
        reg_password = findViewById(R.id.reg_password);
        reg_mail = findViewById(R.id.reg_mail);
        reg_code = findViewById(R.id.reg_code);
        reg_switch = findViewById(R.id.reg_switch);
        reg_find_password = findViewById(R.id.reg_find_password);
        reg_btn = findViewById(R.id.reg_btn);
        reg_get_verify_code = findViewById(R.id.reg_get_verify_code);
        //下面是关于log的监听
        log = findViewById(R.id.log);
        log_account = findViewById(R.id.log_account);
        log_password = findViewById(R.id.log_password);
        log_switch = findViewById(R.id.log_switch);
        log_find_password = findViewById(R.id.log_find_password);
        log_btn = findViewById(R.id.log_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.log_btn:
               account = log_account.getText().toString();
               password = log_password.getText().toString();
                break;
            case R.id.log_find_password:                            //还没写
                break;
            case R.id.log_switch:
                log.setVisibility(View.GONE);
                reg.setVisibility(View.VISIBLE);
                break;
            case R.id.reg_get_verify_code:
                initThread();
            case R.id.reg_btn:                                  //注册
                postRegJsonData();
//            {
//                "user_id": "用户id",
//                    "user_name": "用户名",
//                    "password": "经过加密的密码（加密算法待定）",
//                    "verify_id": "验证码id",
//                    "verify_code": "验证码值"
//            }
//                account = reg_account.getText().toString();
//                password = reg_password.getText().toString();
//                name = getStringRandom();
//                if(verify_id == null){
//                    //可以加图片(尚未美化)
//                    Toast.makeText(this,"请先请求验证码",Toast.LENGTH_LONG).show();
//                }
//                else {
//
//                }


                break;
            case R.id.reg_find_password:                    //找回密码
                break;
            case R.id.reg_switch:
                reg.setVisibility(View.GONE);
                log.setVisibility(View.VISIBLE);
                break;
        }
    }
    //取随机数
    public String getStringRandom() {

        Random random1 = new Random();
        int length = random1.nextInt(10) + 6;
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    //请求验证码
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
    //HttpURLConnection Post发送Json数据，接受返回值。
    private void postRegJsonData(){
        account = reg_account.getText().toString();
        password = reg_password.getText().toString();
        name = getStringRandom();
        if(verify_id == null){
            //可以加图片(尚未美化)
            Toast.makeText(this,"请先请求验证码",Toast.LENGTH_LONG).show();
        }
        else {
            code = reg_code.getText().toString();
            boolean judge = checkCode(code);
            if(judge) {
                try {
//                {
//                    "user_id": "用户id",
//                        "user_name": "用户名",
//                        "password": "经过加密的密码（加密算法待定）",
//                        "verify_id": "验证码id",
//                        "verify_code": "验证码值"
//                }
                    //创建json
                    JSONObject body = new JSONObject();
                    body.put("user_id", getStringRandom());
                    body.put("user_name",name);
                    body.put("password", password);
                    body.put("verify_id",verify_id);
                    body.put("verify_code",code);
                    URL url = new URL("106.13.106.1/account/i/regisit");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    // 设置是否向httpUrlConnection输出
                    conn.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    DataOutputStream os = new DataOutputStream( conn.getOutputStream());
                    String content = String.valueOf(body);
                    os.writeBytes(content);
                    os.flush();
                    os.close();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); //获取输入流
                        StringBuilder response = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int state  = Integer.parseInt(jsonObject.getString("result"));
                        judgeRegState(state);
                        reader.close();
                    }
                    conn.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this,"密码不符合要求，请重新设置密码",Toast.LENGTH_LONG).show();
            }
        }
    }
    //检验密码
    public static boolean checkCode(String code){
        Pattern pattern=Pattern.compile("[\\S]{8,16}$");
        Matcher matcher=pattern.matcher(code);
        return matcher.matches();
    }
    //判断返回状态
    private void judgeRegState(int state){
//        0:注册成功
//        1:用户名重复
//        2:电子邮件已被注册
//        3:验证码错误
//        4:无效的用户名
//        5:无效的密码
//        6:未知错误
        if(state == 0 ){
            Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
        }
        //***************************
        //此处有个逻辑上的bug
        //***************************
        if(state == 1 ){
            Toast.makeText(this,"用户名重复",Toast.LENGTH_LONG).show();
        }
        if(state == 2 ){
            Toast.makeText(this,"电子邮箱已被注册",Toast.LENGTH_LONG).show();
        }
        if(state == 3 ){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state == 4 ){
            Toast.makeText(this,"无效的用户名",Toast.LENGTH_LONG).show();
        }
        if(state == 5 ){
            Toast.makeText(this,"无效的密码",Toast.LENGTH_LONG).show();
        }
        if(state == 6 ){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }

    private void postLogJsonData(){
        account = log_account.getText().toString();
        password = log_password.getText().toString();
        try {
//
//  "account":"用户名",
//  "email":"电子邮件",
//  "password":"加密后的密码(加密算法待定)",
//
                    //创建json
                    JSONObject body = new JSONObject();
                    body.put("email",account);
                    body.put("password",password);
                    URL url = new URL("106.13.106.1/account/i/app_login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    // 设置是否向httpUrlConnection输出
                    conn.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    DataOutputStream os = new DataOutputStream( conn.getOutputStream());
                    String content = String.valueOf(body);
                    os.writeBytes(content);
                    os.flush();
                    os.close();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); //获取输入流
                        StringBuilder response = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int state  = Integer.parseInt(jsonObject.getString("result"));
                        judgeLogState(state);
                        reader.close();
                    }
                    conn.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

    }
    private void judgeLogState(int state){
//        0:登陆成功
//        1:无效用户名
//        2:无效的密码
//        3:验证码错误
//        4:账号被封禁
//        5:已登录
//        6：未知错误
        if(state == 0 ){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
        }
        if(state == 1 ){
            Toast.makeText(this,"无效用户名",Toast.LENGTH_LONG).show();
        }
        if(state == 2 ){
            Toast.makeText(this,"无效的密码",Toast.LENGTH_LONG).show();
        }
        if(state == 3 ){
            Toast.makeText(this,"验证码错误",Toast.LENGTH_LONG).show();
        }
        if(state == 4 ){
            Toast.makeText(this,"账号被封禁",Toast.LENGTH_LONG).show();
        }
        if(state == 5 ){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_LONG).show();
        }
        if(state == 6 ){
            Toast.makeText(this,"未知错误",Toast.LENGTH_LONG).show();
        }
    }
}
