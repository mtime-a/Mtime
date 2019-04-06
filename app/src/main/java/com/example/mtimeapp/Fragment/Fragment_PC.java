package com.example.mtimeapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.ChangePasswordActivity;
import com.example.mtimeapp.MainActivity;
import com.example.mtimeapp.MyCommentsActivity;
import com.example.mtimeapp.Log_RegActivity;
import com.example.mtimeapp.PCActivity;
import com.example.mtimeapp.R;

import java.time.LocalDate;


public class Fragment_PC extends Fragment implements View.OnClickListener {

    private TextView tv_username;//显示用户名称
    private ImageView icon;//显示用户头像
    private TextView jump;//点击文本框跳转到登陆页面或者个人中心页面
    private ImageView setting;//设置
    private TextView comment;
    private TextView about_us;

    private int op = 0;//判断登录状态
    private String name;
    private String nickName;
    private String cookie;
    private String headImage;


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

        jump.setOnClickListener(this);
        setting.setOnClickListener(this);
        icon.setOnClickListener(this);
        comment.setOnClickListener(this);
        about_us.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        name = sharedPreferences.getString("theName", "");
        nickName = sharedPreferences.getString("theNickname", "");
        headImage = "http://132.232.78.106:8001/media/" + sharedPreferences.getString("theHeadImage", "");

//        Bundle bundle = getActivity().getIntent().getExtras();
//
//        if (bundle != null) {
//            name = bundle.getString("username");
//            headImage = "http://132.232.78.106:8001/media/" + bundle.getString("headImage");
//            nickName = bundle.getString("nickName");
//            //tv_username.setText(nickName);
//            Log.e("TAG Resume", nickName);
//            Log.e("TAG Resume", name);
//            Glide.with(this).load(headImage).into(icon);
//        }

        if (cookie.equals("")) {
            op = 0;//未登录
            tv_username.setVisibility(View.GONE);
        } else {
            op = 1;//已经登录
            tv_username.setVisibility(View.VISIBLE);
            tv_username.setText(nickName);
            Glide.with(this).load(headImage).into(icon);
        }
    }


    private void initUI(View view) {
        tv_username = view.findViewById(R.id.pc_username);
        setting = view.findViewById(R.id.pc_setting);
        icon = view.findViewById(R.id.pc_icon);
        jump = view.findViewById(R.id.pc_jump);
        about_us = view.findViewById(R.id.pc_about_us);
        comment = view.findViewById(R.id.pc_comments);
    }

    //这个是设置的菜单控件
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
                //登陆了就跳转到个人中心
                if (op == 1) {
                    intent.setClass(getContext(), PCActivity.class);
                    startActivity(intent);
                }
                //没登陆的时候就跳转到登陆页面
                else ToastAndRemind();

                break;
            case R.id.pc_icon:
                //登陆了就跳转到个人中心
                if (op == 1) {
                    intent.setClass(getContext(), PCActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("nickName", nickName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                //没登陆的时候就跳转到登陆页面
                else ToastAndRemind();

                break;
            case R.id.pc_comments:
                //登陆了就跳转到我的评论
                if (op == 1) {
                    intent.setClass(getContext(), MyCommentsActivity.class);
                    startActivity(intent);
                }
                //没登陆的时候就跳转到登陆页面
                else ToastAndRemind();

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
                        Intent intent = new Intent();
                        switch (item.getItemId()) {
                            case R.id.menu_exit:
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
                                sharedPreferences.edit().clear().commit();
                                intent.setClass(getContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getContext(), "退出成功", Toast.LENGTH_SHORT).show();
                                flag = true;
                                break;
                            case R.id.menu_change:
                                //切换账号时候的操作
                                intent.setClass(getContext(), Log_RegActivity.class);
                                startActivity(intent);
                                flag = true;
                                break;
                            case R.id.menu_password:
                                if (op == 1) {
                                    intent.setClass(getContext(), ChangePasswordActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastAndRemind();
                                }
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

    public void ToastAndRemind() {
        Toast.makeText(getContext(), "还未登陆，请先登录", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(getContext(), Log_RegActivity.class);
        startActivity(intent);
    }
}

