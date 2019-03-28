package com.example.mtimeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mtimeapp.CustomView.RoundImageView;

public class PCActivity extends AppCompatActivity implements View.OnClickListener {
    private RoundImageView icon;
    private TextView username;
    private TextView id;
    private TextView emil;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_homepage);

        initUI();

        initThread();

        initFunc();
    }

    private void initThread() {

    }

    private void initFunc() {
        icon.setOnClickListener(this);
        username.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initUI() {
        icon = findViewById(R.id.pc_homepage_icon);
        username = findViewById(R.id.pc_homepage_username);
        id = findViewById(R.id.pc_homepage_id);
        emil = findViewById(R.id.pc_homepage_emil);
        back = findViewById(R.id.pc_homepage_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pc_homepage_back:
                break;
            case R.id.pc_homepage_icon:
                break;
            case R.id.pc_homepage_username:
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
