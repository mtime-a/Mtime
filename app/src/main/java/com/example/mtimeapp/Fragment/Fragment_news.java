package com.example.mtimeapp.Fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mtimeapp.Adapter.NewsAdapter;
import com.example.mtimeapp.R;
import com.example.mtimeapp.Util.CheckNet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_news extends Fragment {

    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String headId = "0";
    private int headnum = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_news_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.fragment_news_swipe);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new CheckNet(getContext()).initNet()) {
                    headId = "0";
                    initData();
                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                Log.e("FragmentFillm","2");
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部   lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 不可用
                if( lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    //加载数据
                    initData();
                }
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String url = "http://132.232.78.106:8001/api/getNewsList/";
                    List<Map<String, String>> list_url = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("head", headId);
                    map.put("type", "1");
                    map.put("number", "10");
                    list_url.add(map);

                    url = getUrl(url, list_url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("response",responseData);
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    initData();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String JsonData) {
        try {
            list = new ArrayList<>();
            Log.d("mljmlfjasldfjf",JsonData);
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("state");
            if (status.equals("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(i == 0){
                        String num = jsonObject1.getString("id");
                        int num1 = Integer.parseInt(num);
                        //headId = jsonObject1.getString("id");
                        headId = String.valueOf(num1 - 10);
                    }
                    Integer id = Integer.parseInt(jsonObject1.getString("id"));
                    String Title = jsonObject1.getString("Title");
                    String author = jsonObject1.getString("author");
                    String Time = jsonObject1.getString("Time");
                    String photo = jsonObject1.getString("photo");

                    Map map = new HashMap();
                    map.put("id", id);
                    Log.e("id", String.valueOf(id));
                    map.put("Title", Title);
                    map.put("author", author);
                    map.put("Time", Time);
                    map.put("photo", "http://132.232.78.106:8001/media/" + photo);

                    list.add(map);
                }
                showResponse();//子线程进行UI操作
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("mlj", "第2个");
        }
    }

    private void showResponse() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                NewsAdapter adapter = new NewsAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (new CheckNet(getContext()).initNet())
            initData();
    }

    private String getUrl(String url, List<Map<String, String>> list_url) {
        for (int i = 0; i < list_url.size(); i++) {
            Map<String, String> params = list_url.get(i);
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                StringBuffer sb = null;
                while (it.hasNext()) {
                    String key = it.next();
                    String value = params.get(key);
                    if (sb == null) {
                        sb = new StringBuffer();
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += sb.toString();
            }
        }
        Log.d("mljmljmljmlj",url);
        return url;
    }
}
