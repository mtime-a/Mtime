package com.example.mtimeapp.Fragment;

import android.content.Context;
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

//Book是正在售票
public class Fragment_sale_book extends Fragment {

    private RecyclerView recyclerView;
    private List<Map<String, Object>> list;

//    public static Fragment_sale_book newInstance(List<Map<String,Object>> list) {
//        //这个方法你可以将传进来的数据String name改下
//        // 用bundle来运送一些东西到onViewCreated
//        Bundle args = new Bundle();
//        args.putString("name", name);
//        Fragment_PC fragment = new Fragment_PC();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("mlj","加了个Attach bbok");
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

       // initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    list = new ArrayList<>();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://39.96.208.176/film/i/ticketing_film").build();
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
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String image = jsonObject1.getString("image");
                String info = jsonObject1.getString("info");
                String release_date = jsonObject1.getString("release_date");
                String film_id = jsonObject1.getString("film_id");
                String marked_members = jsonObject1.getString("marked_members");
                String commented_members = jsonObject1.getString("commented_members");
                String mark = jsonObject1.getString("mark");

                Map map = new HashMap();
                map.put("title", title);
                map.put("image", "http://39.96.208.176" + image);
                map.put("info", info);
                map.put("film_id", film_id);
                map.put("marked_members", marked_members);
                map.put("commented_members", commented_members);
                map.put("mark", mark);
                map.put("release_date", release_date);

                Log.d("mlj", "book");

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
                BookAdapter adapter = new BookAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}