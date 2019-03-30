package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.R;

import java.util.List;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<Map<String, Object>> list;

    public CommentsAdapter(Context context, List<Map<String, Object>> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, viewGroup, false);
        CommentsAdapter.ViewHolder holder = new CommentsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder viewHolder, int i) {
        Map map = list.get(i);

//        Glide.with(context).load(map.get("author_head")).into(viewHolder.icon);
        viewHolder.name.setText(map.get("author_name").toString());
//        viewHolder.content.setText(map.get("content").toString());
//        viewHolder.time.setText(map.get("time").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView content;
        TextView time;
        CircleImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_comment_name);
            content = itemView.findViewById(R.id.item_comment_content);
            time = itemView.findViewById(R.id.item_comment_time);
            icon = itemView.findViewById(R.id.item_comment_icon);
        }
    }
}
