package com.example.mtimeapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.MyCommentsActivity;
import com.example.mtimeapp.Log_RegActivity;
import com.example.mtimeapp.PCActivity;
import com.example.mtimeapp.R;
import com.example.mtimeapp.ShowActivity;
import com.nostra13.universalimageloader.utils.L;

public class Fragment_PC extends Fragment implements View.OnClickListener {

    private TextView tv_username;//显示用户名称
    private ImageView icon;//显示用户头像
    private TextView jump;//点击文本框跳转到登陆页面或者个人中心页面
    private ImageView setting;//设置
    private TextView comment;
    private TextView about_us;

    private int op = 0;
    private String name;
    private String headImage;
    private String nickName;

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
        //tv_username.setVisibility(View.GONE);//GONE隐藏且不保留所占空间

        jump.setOnClickListener(this);
        setting.setOnClickListener(this);
        icon.setOnClickListener(this);
        comment.setOnClickListener(this);
        about_us.setOnClickListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sps = getActivity().getSharedPreferences("Cookies", Context.MODE_PRIVATE);
        String cookie =  sps.getString("cookie","");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        name =  sharedPreferences.getString("theName","");

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("username");
            headImage = "http://132.232.78.106:8001/media/" + bundle.getString("headImage");
            nickName = bundle.getString("nickName");
            //****************************
            //写不上去，佛了
            // tv_username.setText();
            Log.e("TAG Resume",nickName);
            Log.e("TAG Resume",name);
            Glide.with(this).load(headImage).into(icon);
        }

        if(cookie.equals("")){
            op = 0;
            tv_username.setVisibility(View.GONE);
        }else {
            op = 1;
            tv_username.setText(nickName);
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
                judgeAndIntent(op);
                if(op == 0){
                intent.setClass(getContext(),PCActivity.class);
                startActivity(intent);
                }
                break;
            case R.id.pc_icon:
                //没登陆的时候就跳转到登陆页面
                judgeAndIntent(op);
                //登陆了就跳转到个人中心
                if(op == 0) {
                    Intent intent1 = new Intent(getContext(), PCActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("nickName", nickName);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent1);
                    startActivity(intent1);
                }
                break;
            case R.id.pc_comments:
                //没登陆的时候就跳转到登陆页面
                judgeAndIntent(op);
                //登陆了就跳转到我的评论
                if(op == 1){
                intent.setClass(getContext(), MyCommentsActivity.class);
                startActivity(intent);
                }
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
                        Intent intent1 = new Intent();
                        switch (item.getItemId()) {
                            case R.id.menu_exit:
                                //退出账号时候的操作
                                //*****************
                                //还没写
                                //*****************
                                flag = true;
                                break;
                            case R.id.menu_change:
                                //切换账号时候的操作
                                intent1.setClass(getContext(),Log_RegActivity.class);
                                flag = true;
                                break;
                            case R.id.menu_password:
                                judgeAndIntent(op);
                                if(op == 1){
                                intent1.setClass(getContext(), ChangePasswordActivity.class);
                                startActivity(intent1);
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
    public void judgeAndIntent(int num){
        if(num == 0){
            Toast.makeText(getContext(),"您还未登陆，请先登录",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(getContext(),Log_RegActivity.class);
            startActivity(intent);

        }
    }
}

