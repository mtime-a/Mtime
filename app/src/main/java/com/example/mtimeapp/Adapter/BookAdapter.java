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
import com.example.mtimeapp.Bean.Book;
import com.example.mtimeapp.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {



    //             title":"电影标题",
    //            "image":"缩略图",
    //            "info":"简介",
    //            "release_date":"上映时间",
    //            "film_id":"电影ID",
    //            "mark": "评分"


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle;
        ImageView bookImage;
        TextView bookInfo;
        TextView bookRelease_time;
        TextView bookId;
        TextView bookMark;
        View BookView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BookView = itemView;
            bookTitle = (TextView)itemView.findViewById(R.id.book_title);
            bookImage = (ImageView)itemView.findViewById(R.id.book_picture);
            bookInfo = (TextView)itemView.findViewById(R.id.book_info);
            bookRelease_time = (TextView)itemView.findViewById(R.id.book_date);
            bookMark = (TextView) itemView.findViewById(R.id.book_score);

        }
    }

    private Context context;
    private List<Book> list;

    public BookAdapter(Context context, List<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, viewGroup, false);
        BookAdapter.ViewHolder holder = new BookAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder viewHolder, int i) {
        Book book = list.get(i);
        Glide.with(context).load(book.getBookImage()).into(viewHolder.bookImage);
        viewHolder.bookTitle.setText(book.getBookTitle());
        viewHolder.bookMark.setText(book.getBookMark());
        viewHolder.bookRelease_time.setText(book.getBookRelease_time());
        viewHolder.bookInfo.setText(book.getBookInfo());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void sendmassage(){

    }
}
