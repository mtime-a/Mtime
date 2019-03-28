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
import com.example.mtimeapp.Log_RegActivity;
import com.example.mtimeapp.R;
import com.example.mtimeapp.ShowActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, Object>> list;

    public ShowAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_show, viewGroup, false);
        ShowAdapter.ViewHolder holder = new ShowAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAdapter.ViewHolder holder, int i) {
        Map map = list.get(i);

        holder.title.setText(map.get("title").toString());
//        holder.date.setText(map.get("date").toString());
//        holder.info.setText(map.get("info").toString());
//        Glide.with(context).load(map.get("image").toString()).into(holder.picture);
//        holder.mark.setText(map.get("mark").toString());
//        holder.date.setText(map.get("release_date").toString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, ShowActivity.class);
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
        TextView info;
        TextView date;
        TextView mark;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_sale_show_card);
            title = itemView.findViewById(R.id.item_sale_show_title);
            picture = itemView.findViewById(R.id.item_sale_show_picture);
            info = itemView.findViewById(R.id.item_sale_show_info);
            date = itemView.findViewById(R.id.item_sale_show_date);
            mark = itemView.findViewById(R.id.item_sale_show_mark);
        }
    }
}