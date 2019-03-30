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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.BookActivity;
import com.example.mtimeapp.CustomView.RoundImageView;
import com.example.mtimeapp.FilmActivity;
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
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder viewHolder, final int i) {
        Map map = list.get(i);

        viewHolder.title.setText(map.get("title").toString());
        Glide.with(context).load(map.get("image")).into(viewHolder.picture);
        viewHolder.date.setText(map.get("release_date").toString());
        viewHolder.mark.setText(map.get("mark").toString());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, BookActivity.class);
                intent.putExtra("film_id",list.get(i).get("film_id").toString());
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
        TextView date;
        TextView title;
        TextView mark;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_sale_book_card);
            picture = itemView.findViewById(R.id.item_sale_book_picture);
            date = itemView.findViewById(R.id.item_sale_book_date);
            title = itemView.findViewById(R.id.item_sale_book_title);
            mark = itemView.findViewById(R.id.item_sale_book_mark);

        }
    }
}
