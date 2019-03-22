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

public class Fragment_sale_show extends Fragment {
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

        //initThread();
        list=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map map = new HashMap();
            map.put("title", "mlj" + i);
            list.add(map);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final BookAdapter adapter = new BookAdapter(getContext(), list);///需要传入什么东西
        recyclerView.setAdapter(adapter);

    }

    private void initThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/film/i/ticketing_film").build();
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
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String showTitle = jsonObject1.getString("title");
                String showImage = jsonObject1.getString("image");
                String showInfo = jsonObject1.getString("info");
                String showFilmId = jsonObject1.getString("film_id");
                String showMark = jsonObject1.getString("mark");

                Map map = new HashMap();
                map.put("title", showTitle);
                map.put("image", showImage);
                map.put("info", showInfo);
                map.put("film_id", showFilmId);
                map.put("mark", showMark);
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
                ShowAdapter adapter = new ShowAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);

            }
        });
    }

}
