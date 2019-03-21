package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.RoundImageView;
import com.example.mtimeapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private Context context;
    private List<Map<String, Object>> list;

    public BookAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_book, viewGroup, false);
        BookAdapter.ViewHolder holder = new BookAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder viewHolder, int i) {
        Map map = list.get(i);

        Glide.with(context).load(map.get("picture")).into(viewHolder.picture);
        viewHolder.title.setText(map.get("title").toString());
        viewHolder.date.setText(map.get("date").toString());
        viewHolder.info.setText(map.get("info").toString());
        viewHolder.mark.setText(map.get("mark").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundImageView picture;
        TextView date;
        TextView info;
        TextView title;
        TextView mark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.item_sale_book_picture);
            date = itemView.findViewById(R.id.item_sale_book_date);
            info = itemView.findViewById(R.id.item_sale_book_info);
            title = itemView.findViewById(R.id.item_sale_book_title);
            mark = itemView.findViewById(R.id.item_sale_book_mark);

        }
    }
}
