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

        if (new CheckNet(getContext()).initNet())
            initData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new CheckNet(getContext()).initNet()) {
                    initData();
                    Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://132.232.78.106:8001/api/getNewsList/")
                            .addHeader("head","0")
                            .addHeader("type","6")
                            .addHeader("number","9")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
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
                    Integer id = Integer.parseInt(jsonObject1.getString("id"));
                    String Title = jsonObject1.getString("Title");
                    String author = jsonObject1.getString("author");
                    String Time = jsonObject1.getString("Time");
                    String photo = jsonObject1.getString("photo");

                    Map map = new HashMap();
                    map.put("id", id);
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

}
