package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, Object>> list;


    public FilmAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FilmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, viewGroup, false);
        FilmAdapter.ViewHolder holder = new FilmAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FilmAdapter.ViewHolder viewHolder, int i) {
        Map map = new HashMap();

        Glide.with(context).load(map.get("picture").toString()).into(viewHolder.picture);                  //展示图片，标题，发布时间。
        viewHolder.title.setText(map.get("title").toString());
        viewHolder.info.setText(map.get("info").toString());
        viewHolder.mark.setText(map.get("mark").toString());
        viewHolder.date.setText(map.get("date").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView title;
        TextView info;
        TextView date;
        TextView mark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.item_film_picture);
            title = itemView.findViewById(R.id.item_film_title);
            info = itemView.findViewById(R.id.item_film_info);
            date = itemView.findViewById(R.id.item_film_date);
            mark = itemView.findViewById(R.id.item_film_mark);
        }
    }
}


