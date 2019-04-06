package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.CustomView.RoundImageView;
import com.example.mtimeapp.FilmActivity;
import com.example.mtimeapp.Log_RegActivity;
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
    public void onBindViewHolder(@NonNull FilmAdapter.ViewHolder viewHolder, final int i) {
        Map map = list.get(i);

        viewHolder.title.setText(map.get("title").toString());
        Glide.with(context).load(map.get("poster")).into(viewHolder.picture);
        Glide.with(context).load(map.get("author_head").toString()).into(viewHolder.author_head);
        viewHolder.subtitle.setText(map.get("subtitle").toString());
        viewHolder.author_name.setText(map.get("author_name").toString());
        viewHolder.comment_num.setText(map.get("comment_num").toString());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, FilmActivity.class);
                intent.putExtra("comment_id", list.get(i).get("comment_id").toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundImageView picture;
        TextView title;
        TextView subtitle;
        TextView author_name;
        RoundImageView author_head;
        CardView cardView;
        TextView comment_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_film_card);
            picture = itemView.findViewById(R.id.item_film_picture);
            title = itemView.findViewById(R.id.item_film_title);
            subtitle = itemView.findViewById(R.id.item_film_subtitle);
            author_name = itemView.findViewById(R.id.item_film_username);
            comment_num = itemView.findViewById(R.id.item_film_commentnum);
            author_head = itemView.findViewById(R.id.item_film_icon);
        }
    }
}


