package com.example.mtimeapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mtimeapp.Adapter.BookAdapter;
import com.example.mtimeapp.Adapter.ShowAdapter;
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

//这个是即将上映
public class Fragment_sale_show extends Fragment {
    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;
    private String title;
    private String image;
    private String info;
    private String film_id;
    private String mark;
    private String marked_members;
    private String commented_members;
    private String release_date;

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

        initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://39.96.208.176/film/i/coming_film").build();
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
                    title = jsonObject1.getString("title");
                    image = jsonObject1.getString("image");
                    info = jsonObject1.getString("info");
                    film_id = jsonObject1.getString("film_id");
                    mark = jsonObject1.getString("mark");
                    marked_members = jsonObject1.getString("marked_members");
                    commented_members = jsonObject1.getString("commented_members");
                    release_date = jsonObject1.getString("release_date");

                    Map map = new HashMap();
                    map.put("title", title);
                    map.put("image", "http://39.96.208.176" + image);
                    map.put("info", info);
                    map.put("film_id", film_id);
                    map.put("mark", mark);
                    map.put("marked_members", marked_members);
                    map.put("commented_members", commented_members);
                    map.put("release_date", release_date);

                    Log.d("mlj", "show");

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
                ShowAdapter adapter = new ShowAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
