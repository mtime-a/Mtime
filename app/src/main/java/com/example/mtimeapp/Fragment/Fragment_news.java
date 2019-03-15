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
import com.example.mtimeapp.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_news extends Fragment {

    private RecyclerView recyclerView;
    private String name;

    public static Fragment_news newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        Fragment_news fragment = new Fragment_news();
        fragment.setArguments(args);
        return fragment;
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

        recyclerView = view.findViewById(R.id.news_recyclerview);

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.get("name").toString();
        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(name + i);
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final NewsAdapter adapter = new NewsAdapter(getContext(), list);///需要传入什么东西
        recyclerView.setAdapter(adapter);

    }
}
