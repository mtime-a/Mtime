package com.example.mtimeapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mtimeapp.Adapter.ViewPagerAdapter;
import com.example.mtimeapp.CustomView.FatherViewPager;
import com.example.mtimeapp.Fragment.Fragment_PC;
import com.example.mtimeapp.Fragment.Fragment_news;
import com.example.mtimeapp.Fragment.Fragment_sale;
import com.example.mtimeapp.Fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private FatherViewPager viewPager;
    private MenuItem menuItem;
    private TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定监听
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager = findViewById(R.id.vp);

        //底部导航栏的点击
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //切换页面
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //设置适配器
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        //添加Fragment
        List<Fragment> list = new ArrayList<>();


        /*
        ***********************************

        下面可以修改

        ***********************************
         */


        list.add(TestFragment.newInstance("今日热点"));
        list.add(Fragment_news.newInstance("马良及"));
        list.add(Fragment_sale.newInstance("卡片"));
        list.add(Fragment_PC.newInstance("行者"));

        //设置Adapter
        viewPagerAdapter.setList(list);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //初始化图片
            resetToDefaultIcon();

            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    viewPager.setCurrentItem(0);
                    item.setIcon(R.mipmap.sanjiaoxing2);
                    return true;
                case R.id.navigation_film:
                    viewPager.setCurrentItem(1);
                    item.setIcon(R.mipmap.tu4);
                    return true;
                case R.id.navigation_sale:
                    viewPager.setCurrentItem(2);
                    item.setIcon(R.mipmap.wubianxing2);
                    return true;
                case R.id.navigation_pc:
                    viewPager.setCurrentItem(3);
                    item.setIcon(R.mipmap.tu2);
                    return true;
            }
            return false;
        }
    };

    //初始化图片的方法//
    private void resetToDefaultIcon() {
        MenuItem news = bottomNavigationView.getMenu().findItem(R.id.navigation_news);
        news.setIcon(R.mipmap.sanjiaoxing1);

        MenuItem film = bottomNavigationView.getMenu().findItem(R.id.navigation_film);
        film.setIcon(R.mipmap.tu3);

        MenuItem sale = bottomNavigationView.getMenu().findItem(R.id.navigation_sale);
        sale.setIcon(R.mipmap.wubianxing1);

        MenuItem pc = bottomNavigationView.getMenu().findItem(R.id.navigation_pc);
        pc.setIcon(R.mipmap.tu1);
    }
}