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
import com.example.mtimeapp.Bean.Book;
import com.example.mtimeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_sale_book extends Fragment {

    private RecyclerView recyclerView;
    private List<Book>bookList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {                                                                                          //okHttp请求数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("106.13.106.1/film/i/coming_film").build();
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
        View view = inflater.inflate(R.layout.fragment_sale_book, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        recyclerView = view.findViewById(R.id.sale_book_recyclerview);
//
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add("mlj"+i);
//        }
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);
//        final BookAdapter adapter = new BookAdapter(getContext(),bookList);///需要传入什么东西
//        recyclerView.setAdapter(adapter);
    }
    private void  parseJSONWithJSONObject(String JsonData) {               //解析JSON数据




        //   接口     "title":"电影标题",
        //            "image":"缩略图",
        //            "info":"简介",
        //            "release_date":"上映时间",
        //            "film_id":"电影ID",
        //            "mark": "评分"

        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            String status = jsonObject.getString("status");
            bookList.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if(status.equals("ok")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String bookTitle = jsonObject1.getString("title");
                    String bookImage = jsonObject1.getString("image");
                    String bookInfo = jsonObject1.getString("info");
                    String bookRelease_date = jsonObject1.getString("release_time");
                    String bookId = jsonObject1.getString("film_id");
                    String bookMark = jsonObject1.getString("mark");
                    Book book = new Book();
                    book.setBookTitle(bookTitle);
                    book.setBookImage(bookImage);
                    book.setBookInfo(bookInfo);
                    book.setBookRelease_time(bookRelease_date);
                    book.setBookId(bookId);
                    bookList.add(book);
                }
            }
            if(status.equals(""))
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        private void showResponse() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView = getView().findViewById(R.id.sale_book_recyclerview);               //初始化recycleView
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(manager);
                    BookAdapter adapter = new BookAdapter(getContext(),bookList);
                    recyclerView.setAdapter(adapter);

                }
            });
        }
}

