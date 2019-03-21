package com.example.mtimeapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mtimeapp.Adapter.ShowAdapter;
import com.example.mtimeapp.Bean.Show;
import com.example.mtimeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_sale_show extends Fragment {
    private RecyclerView recyclerView;
    private List<Show> showList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/film/i/ticketing_film").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);                                                 //解析json的方法

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } }).start();
    }
    private void  parseJSONWithJSONObject(String JsonData) {               //解析JSON数据

        //"num":"数量(int)",
//        "list":[{
//        "title":"电影标题",
//                "image":"缩略图",
//                "info":"简介",
//                "release_date":"上映时间",
//                "film_id":"电影ID",
//                "mark": "评分"
//    }],
//            "status": "ok"

        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            showList.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String showTitle = jsonObject1.getString("title");
                String showImage = jsonObject1.getString("image");
                String showInfo = jsonObject1.getString("info");
                String showFilmId = jsonObject1.getString("film_id");
                String showMark = jsonObject1.getString("mark");
                Show show = new Show();
                show.setShowTitle(showTitle);
                show.setShowImage(showImage);
                show.setShowInfo(showInfo);
                show.setShowFilm_id(showFilmId);
                show.setShowMark(showMark);
                showList.add(show);
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
                recyclerView = getView().findViewById(R.id.sale_show_recyclerview);               //初始化recycleView
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                ShowAdapter adapter = new ShowAdapter(getContext(),showList);
                recyclerView.setAdapter(adapter);

            }
        });
    }
}
