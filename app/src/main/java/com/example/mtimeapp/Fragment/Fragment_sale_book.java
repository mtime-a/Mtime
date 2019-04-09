package com.example.mtimeapp.Fragment;

import android.content.Context;
import android.net.Uri;
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

import com.example.mtimeapp.Adapter.BookAdapter;
import com.example.mtimeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//Book是正在售票
public class Fragment_sale_book extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Map<String, Object>> list;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_book, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.book_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.book_swipe);

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    HttpUrl url=HttpUrl.parse("http://132.232.78.106:8001/api/getFilmList/");
                    assert url != null;
                    url.newBuilder()
                            .addQueryParameter("type","0")
                            .addQueryParameter("head","0")
                            .addQueryParameter("number","3")
                            .build();
                    Request request=new Request.Builder()
                            .url(url)
//                            .addHeader("head", "1")
//                            .addHeader("type", "1")
//                            .addHeader("number", "3")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);

//                    OkHttpClient client = new OkHttpClient();
//                    Request request = new Request.Builder().url("http://132.232.78.106:8001/api/getFilmList/")
//                            .addHeader("head", "1")
//                            .addHeader("type", "1")
//                            .addHeader("number", "3")
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    parseJSONWithJSONObject(responseData);
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
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("state");
            if (status.equals("1")) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String film_id = jsonObject1.getString("film_id");
                    String title = jsonObject1.getString("title");
                    String mark = jsonObject1.getString("mark");
                    String release_date = jsonObject1.getString("release_date");
                    String image = jsonObject1.getString("image");
                    String info = jsonObject1.getString("info");

                    Map<String, Object> map = new HashMap();
                    map.put("info", info);
                    map.put("film_id", film_id);
                    map.put("title", title);
                    map.put("mark", mark);
                    map.put("release_date", release_date);
                    map.put("image", "http://132.232.78.106:8001" + image);

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
                BookAdapter adapter = new BookAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}