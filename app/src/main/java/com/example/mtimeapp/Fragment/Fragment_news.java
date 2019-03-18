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
import com.example.mtimeapp.Bean.HotNews;
import com.example.mtimeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_news extends Fragment {

    private RecyclerView recyclerView;
    private String name;
    private List<HotNews>mlist;
    private List<HotNews>newsList = new ArrayList<>();

    public static Fragment_news newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        Fragment_news fragment = new Fragment_news();
        fragment.setArguments(args);
        return fragment;
    }

               //*******************************************************
               //    这页都是我自己的习惯来的，结构可能有点诡异。
               //           接口还不能用，大概会有不少bug
               //*******************************************************
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            } }).start();
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
        Bundle bundle = getArguments();                                                                //用Bundle传值，传了啥？之前代码还没翻
        if (bundle != null) {
            name = bundle.get("name").toString();
        }



//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add(name + i);
//        }
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());                           //改到转回主线程之后了
//        recyclerView.setLayoutManager(manager);
//        final NewsAdapter adapter = new NewsAdapter(getContext(),mlist);
//        recyclerView.setAdapter(adapter);

    }
    private void  parseJSONWithJSONObject(String JsonData) {               //解析JSON数据

        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            mlist.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int newsId = Integer.parseInt(jsonObject1.getString("news_id"));
                String newsTitle = jsonObject1.getString("title");
                String pub_time = jsonObject1.getString("pub_time");
                String newsImage = jsonObject1.getString("picture");
                HotNews hotNews = new HotNews();
                hotNews.setNewsTitle(newsTitle);
                hotNews.setNewsImage(newsImage);
                hotNews.setPub_time(pub_time);
                hotNews.setNewsId(newsId);
                newsList.add(hotNews);
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
                recyclerView = getView().findViewById(R.id.news_recyclerview);               //初始化recycleView
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                NewsAdapter adapter = new NewsAdapter(getContext(),mlist);
                recyclerView.setAdapter(adapter);

            }
        });
    }
}
