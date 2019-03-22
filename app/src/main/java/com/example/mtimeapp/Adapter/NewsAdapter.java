package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.FilmActivity;
import com.example.mtimeapp.NewsActivity;
import com.example.mtimeapp.R;

import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private Context context;
    private List<Map<String, Object>> list;

    public NewsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
        NewsAdapter.ViewHolder holder = new NewsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder viewHolder, int i) {
        Map map = list.get(i);

        viewHolder.title.setText(map.get("title").toString());
//        Glide.with(context).load(map.get("picture")).into(viewHolder.picture);
//        viewHolder.date.setText(map.get("date").toString());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, NewsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView title;
        TextView date;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_news_card);
            title = itemView.findViewById(R.id.item_news_title);
            picture = itemView.findViewById(R.id.item_news_picture);
            date = itemView.findViewById(R.id.item_news_date);
        }
    }
}



