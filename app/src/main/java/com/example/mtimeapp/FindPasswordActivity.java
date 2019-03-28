package com.example.mtimeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mSend;
    private EditText mAccount;
    private EditText mEmil;
    private EditText mCode;
    private EditText mPassword;
    private Button btn_commit;
    private Button btn_finsh;
    private ImageView close;

    private String email;
    private String account;
    private String password;
    private String code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        initUI();

        initClick();

        btn_finsh.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);

    }

    private void initClick() {
        mSend.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        btn_finsh.setOnClickListener(this);
        close.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_password_close:
                finish();
                break;
            case R.id.find_password_send:
                //进行发送验证码请求
                email = mEmil.getText().toString();
                break;
            case R.id.find_password_btn_commit:
                btn_finsh.setVisibility(View.VISIBLE);
                mPassword.setVisibility(View.VISIBLE);

                btn_commit.setVisibility(View.GONE);
                mCode.setVisibility(View.GONE);
                mAccount.setVisibility(View.GONE);
                mEmil.setVisibility(View.GONE);
                //上面是隐藏与显示某些控件

                //下面判断是不是能修改
                code = mCode.getText().toString();
                account = mAccount.getText().toString();
                break;
            case R.id.find_password_btn_finsh:
                //修改密码同时post上去
                password = mPassword.getText().toString();
                break;
        }
    }

    private void initUI() {
        mSend = findViewById(R.id.find_password_send);
        mAccount = findViewById(R.id.find_password_account);
        mEmil = findViewById(R.id.find_password_mail);
        mCode = findViewById(R.id.find_password_code);
        mPassword = findViewById(R.id.find_password_password);
        btn_commit = findViewById(R.id.find_password_btn_commit);
        btn_finsh = findViewById(R.id.find_password_btn_finsh);
        close = findViewById(R.id.find_password_close);
    }
}
