package com.example.mtimeapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Map<String, Object>> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.fragment_film_swipe);
        recyclerView = view.findViewById(R.id.fragment_film_recyclerview);

        initData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://132.232.78.106:8001/api/getHotFilmReview/").build();
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
            String status = jsonObject.getString("state");
            if (status.equals("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String comment_id = jsonObject1.getString("comment_id");
                    String title = jsonObject1.getString("title");
                    String subtitle = jsonObject1.getString("subtitle");
                    String author_name = jsonObject1.getString("author_name");
                    String author_head = jsonObject1.getString("author_head");
                    String image = jsonObject1.getString("image");
                    String poster = jsonObject1.getString("poster");
                    String comment_num = jsonObject1.getString("comment_num");

                    Log.d("mlj", "tupian" + poster);

                    Map<String, Object> map = new HashMap();
                    map.put("comment_id", comment_id);
                    map.put("title", title);
                    map.put("subtitle", subtitle);
                    map.put("comment_num", comment_num);
                    map.put("author_name", author_name);
                    map.put("author_head", "http://132.232.78.106:8001" + author_head);
                    map.put("poster", "http://132.232.78.106:8001" + poster);//海报

                    Log.d("FilmFragment commentId", comment_id);
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
                FilmAdapter adapter = new FilmAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}