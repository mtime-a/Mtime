package com.example.mtimeapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mtimeapp.Log_RegActivity;
import com.example.mtimeapp.R;

public class Fragment_PC extends Fragment implements View.OnClickListener {

    private TextView tv_username;//显示用户名称
    private ImageView icon;//显示用户头像
    private TextView jump;//点击文本框跳转到登陆页面或者个人中心页面
    private ImageView setting;//设置

//    public static Fragment_PC newInstance(String name) {
//        //这个方法你可以将传进来的数据String name改下
//        // 用bundle来运送一些东西到onViewCreated
//        Bundle args = new Bundle();
//        args.putString("name", name);
//        Fragment_PC fragment = new Fragment_PC();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pc, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_username = view.findViewById(R.id.pc_username);
        setting = view.findViewById(R.id.pc_setting);
        icon = view.findViewById(R.id.pc_icon);
        jump = view.findViewById(R.id.pc_jump);

        //如果没登陆就隐藏用户名控件 并且将jump中文字更改
        tv_username.setVisibility(View.GONE);//GONE隐藏且不保留所占空间

        jump.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            tv_username.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pc_jump:
                Intent intent = new Intent();
                intent.setClass(getContext(), Log_RegActivity.class);
                startActivity(intent);
                break;
        }
    }
}

