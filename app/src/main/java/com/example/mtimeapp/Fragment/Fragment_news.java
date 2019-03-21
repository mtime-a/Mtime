package com.example.mtimeapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mtimeapp.Adapter.NewsAdapter;
import com.example.mtimeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_news extends Fragment {

    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;

    public static Fragment_news newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        Fragment_news fragment = new Fragment_news();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.news_recyclerview);

        initThread();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final NewsAdapter adapter = new NewsAdapter(getContext(), list);///需要传入什么东西
        recyclerView.setAdapter(adapter);
    }

    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/news/i/hotpot_list").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String JsonData) {
        try {
            list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            if (status.equals("ok")) {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    int newsId = Integer.parseInt(jsonObject1.getString("news_id"));
                    String newsTitle = jsonObject1.getString("title");
                    String pub_time = jsonObject1.getString("pub_time");
                    String newsImage = jsonObject1.getString("picture");

                    Map map = new HashMap();
                    map.put("news_id", newsId);
                    map.put("title", newsTitle);
                    map.put("pub_time", pub_time);
                    map.put("picture", newsImage);
                    list.add(map);
                }
                showResponse();
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
