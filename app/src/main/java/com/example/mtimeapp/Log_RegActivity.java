package com.example.mtimeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Log_RegActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout reg;
    private EditText reg_account;
    private EditText reg_password;
    private EditText reg_mail;
    private EditText reg_code;
    private Button reg_btn;
    private TextView reg_switch;
    private TextView reg_find_password;

    private LinearLayout log;
    private EditText log_account;
    private EditText log_password;
    private Button log_btn;
    private TextView log_switch;
    private TextView log_find_password;

    private ImageView close;

    private String accout;
    private String password;
    private String email;
    private String code;

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
                break;
            case R.id.log_btn:
                accout = log_account.getText().toString();
                password = log_password.getText().toString();
                break;
            case R.id.log_find_password:
                break;
            case R.id.log_switch:
                log.setVisibility(View.GONE);
                reg.setVisibility(View.VISIBLE);
                break;
            case R.id.reg_btn:
                break;
            case R.id.reg_find_password:
                break;
            case R.id.reg_switch:
                reg.setVisibility(View.GONE);
                log.setVisibility(View.VISIBLE);
                break;
        }
    }
}
