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

import com.example.mtimeapp.Adapter.FilmAdapter;
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

public class Fragment_film extends Fragment {

    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;

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

        //initThread();

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map map = new HashMap();
            map.put("title", "mlj" + i);
            list.add(map);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final FilmAdapter adapter = new FilmAdapter(getContext(), list);///需要传入什么东西
        recyclerView.setAdapter(adapter);
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/news/i/hotpot_list").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);                                                 //解析json的方法

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String JsonData) {               //解析JSON数据

        try {
            list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            //注意状态
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String comment_id = jsonObject1.getString("comment_id");
                String filmTitle = jsonObject1.getString("title");
                String filmSubtitle = jsonObject1.getString("subtitle");
                String filmImage = jsonObject1.getString("image");
                String author_id = jsonObject1.getString("author_id");
                String author_name = jsonObject1.getString("author_name");
                String author_head = jsonObject1.getString("author_head");
                String comment_num = jsonObject1.getString("comment_num");

                Map map = new HashMap();
                map.put("title", filmTitle);
                map.put("subtitle", filmSubtitle);
                map.put("image", filmImage);
                map.put("comment_id", comment_id);
                map.put("author_id", author_id);
                map.put("author_name", author_name);
                map.put("author_head", author_head);
                map.put("comment_num", comment_num);

                list.add(map);
            }
            showResponse();
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
                FilmAdapter adapter = new FilmAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);

            }
        });
    }
}
