package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mtimeapp.CustomView.RoundImageView;
import com.example.mtimeapp.R;

import java.util.List;
import java.util.Map;

public class MyCommentsAdapter extends RecyclerView.Adapter<MyCommentsAdapter.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> list;

    public MyCommentsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mycomment, viewGroup, false);
        MyCommentsAdapter.ViewHolder holder = new MyCommentsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentsAdapter.ViewHolder viewHolder, int i) {
        final Map map = list.get(i);

        viewHolder.title.setText(map.get("title").toString());
//        Glide.with(context).load(map.get("image")).into(viewHolder.image);
//        viewHolder.content.setText(map.get("content").toString());
//
//        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                if (Integer.parseInt(map.get("type").toString()) == 0)
//                    intent.setClass(context, NewsActivity.class);
//                else intent.setClass(context, FilmActivity.class);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundImageView image;
        TextView title;
        TextView content;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_mycomment_image);
            title = itemView.findViewById(R.id.item_mycomment_title);
            content = itemView.findViewById(R.id.item_mycomment_content);
            layout = itemView.findViewById(R.id.item_mycomment_layout);
        }
    }
}
