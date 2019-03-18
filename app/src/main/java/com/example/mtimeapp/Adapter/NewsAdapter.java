package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.Bean.HotNews;
import com.example.mtimeapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private Context context;
    private List<HotNews> list;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
        TextView pub_time;
        View NewsView;

        public ViewHolder(View view) {
            super(view);
            NewsView = view;
            newsTitle = (TextView)view.findViewById(R.id.news_title);       //新闻标题
            newsImage = (ImageView)view.findViewById(R.id.news_image);              //新闻图片
            pub_time = (TextView)view.findViewById(R.id.news_pub_time);         //新闻发布时间
        }
    }

    public NewsAdapter(Context context, List<HotNews> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context == null){
            context = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsTitle.setOnClickListener(new View.OnClickListener() {                 //所有的部件点击后跳转至具体内容页面
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                HotNews hotNews = list.get(i);
                int newsId = hotNews.getNewsId();
                // Intent intent = new Intent(view.getContext(),.class);                 //传递Id
                // intent.putExtra("newsId",newsId);
                // view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                HotNews hotNews = list.get(i);
                int newsId = hotNews.getNewsId();
                // Intent intent = new Intent(view.getContext(),.class);
                // intent.putExtra("newsId",newsId);
                // view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.pub_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                HotNews hotNews = list.get(i);
                int newsId = hotNews.getNewsId();
                // Intent intent = new Intent(view.getContext(),.class);
                // intent.putExtra("newsId",newsId);
                // view.getContext().startActivities(new Intent[] {intent});
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder viewHolder, int i) {
        HotNews hotNews = list.get(i);
        Glide.with(context).load(hotNews. getNewsImage()).into(viewHolder.newsImage);                  //展示图片，标题，发布时间。
        viewHolder.pub_time.setText(hotNews.getPub_time());
        viewHolder.newsTitle.setText(hotNews.getNewsTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
