package com.example.mtimeapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mtimeapp.ChangePasswordActivity;
import com.example.mtimeapp.MyCommentsActivity;
import com.example.mtimeapp.Log_RegActivity;
import com.example.mtimeapp.PCActivity;
import com.example.mtimeapp.R;
import com.example.mtimeapp.ShowActivity;

public class Fragment_PC extends Fragment implements View.OnClickListener {

    private TextView tv_username;//显示用户名称
    private ImageView icon;//显示用户头像
    private TextView jump;//点击文本框跳转到登陆页面或者个人中心页面
    private ImageView setting;//设置
    private TextView comment;
    private TextView about_us;

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
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        //如果没登陆就隐藏用户名控件 并且将jump中文字更改
        tv_username.setVisibility(View.GONE);//GONE隐藏且不保留所占空间

        jump.setOnClickListener(this);
        setting.setOnClickListener(this);
        icon.setOnClickListener(this);
        comment.setOnClickListener(this);
        about_us.setOnClickListener(this);

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String name = bundle.get("name").toString();
//            tv_username.setText(name);
//        }
    }

    private void initUI(View view) {
        tv_username = view.findViewById(R.id.pc_username);
        setting = view.findViewById(R.id.pc_setting);
        icon = view.findViewById(R.id.pc_icon);
        jump = view.findViewById(R.id.pc_jump);
        about_us = view.findViewById(R.id.pc_about_us);
        comment = view.findViewById(R.id.pc_comments);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.pc_jump:
                //没登陆的时候就跳转到登陆页面
                intent.setClass(getContext(), PCActivity.class);
                //登陆了就跳转到个人中心
                //intent.setClass(getContext(),PCActivity.class);
                startActivity(intent);
                break;
            case R.id.pc_icon:
                //没登陆的时候就跳转到登陆页面
                intent.setClass(getContext(), Log_RegActivity.class);
                //登陆了就跳转到个人中心
                //intent.setClass(getContext(),PCActivity.class);
                startActivity(intent);
                break;
            case R.id.pc_comments:
                //没登陆的时候就跳转到登陆页面
                //intent.setClass(getContext(), Log_RegActivity.class);
                //登陆了就跳转到我的评论
                intent.setClass(getContext(), MyCommentsActivity.class);
                startActivity(intent);
                break;
            case R.id.pc_about_us:
                //intent.setClass(getContext(),AboutmeActivity.class);
                break;
            case R.id.pc_setting:
                PopupMenu menu = new PopupMenu(getContext(), v);
                menu.getMenuInflater().inflate(R.menu.menu_setting, menu.getMenu());
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        boolean flag;
                        switch (item.getItemId()) {
                            case R.id.menu_exit:
                                //退出账号时候的操作
                                flag = true;
                                break;
                            case R.id.menu_change:
                                //切换账号时候的操作
                                flag = true;
                                break;
                            case R.id.menu_password:
                                Intent intent1 = new Intent();
                                intent1.setClass(getContext(), ChangePasswordActivity.class);
                                startActivity(intent1);
                                //修改密码时候的操作
                                flag = true;
                                break;
                            default:
                                flag = false;
                        }
                        return flag;
                    }
                });
                break;
        }
    }
}

