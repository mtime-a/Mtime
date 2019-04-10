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

import com.example.mtimeapp.Adapter.BookAdapter;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//这个是即将上映
public class Fragment_sale_show extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_show, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.show_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.show_swipe);

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
                    String url = "http://132.232.78.106:8001/api/getFilmList/";
                    List<Map<String, String>> list_url = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("head", "0");
                    map.put("type", "0");
                    map.put("number", "3");
                    list_url.add(map);

                    url = getUrl(url, list_url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url)
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
        return url;
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

                    Log.d("mlj", title);

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
