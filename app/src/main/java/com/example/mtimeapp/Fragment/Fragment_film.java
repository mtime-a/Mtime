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
import com.example.mtimeapp.Bean.Film;
import com.example.mtimeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_film extends Fragment {
    private RecyclerView recyclerView;
  //  private List<Film> mlist;
    private List<Film> filmList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/film/i/hot_reviews_list").build();
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
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void  parseJSONWithJSONObject(String JsonData) {               //解析JSON数据

        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            filmList.clear();
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
                    Film film = new Film();
                    film.setFilmTitle(filmTitle);
                    film.setFilmSubTitle(filmSubtitle);
                    film.setFilmImage(filmImage);
                    film.setFilm_com_id(comment_id);
                    film.setAut_id(author_id);
                    film.setAut_head(author_head);
                    film.setAut_name(author_name);
                    film.setComment_num(comment_num);
                    filmList.add(film);
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
                recyclerView = getView().findViewById(R.id.fragment_film_recyclerview);               //初始化recycleView
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                FilmAdapter adapter = new FilmAdapter(getContext(),filmList);
                recyclerView.setAdapter(adapter);

            }
        });
    }
}
