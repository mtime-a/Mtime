package com.example.mtimeapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mtimeapp.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_sale extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment_sale_book saleBook;
    private Fragment_sale_show saleShow;

    private List<Fragment> list_view;
    private List<String> list_title;

//    public static Fragment_sale newInstance(String name) {
//        Bundle args = new Bundle();
//        args.putString("name", name);
//        Fragment_sale fragment = new Fragment_sale();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.sale_vp);

        saleBook = new Fragment_sale_book();
        saleShow = new Fragment_sale_show();

        list_view = new ArrayList<>();
        list_title = new ArrayList<>();

        list_view.add(saleBook);
        list_view.add(saleShow);

        list_title.add("即将上映");
        list_title.add("正在售票");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            //获取每个页卡
            public android.support.v4.app.Fragment getItem(int i) {
                return list_view.get(i);
            }

            @Override
            //获取页卡数
            public int getCount() {
                return list_view.size();
            }

            @Nullable
            @Override
            //获取页卡标题
            public CharSequence getPageTitle(int position) {
                return list_title.get(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }
}

