package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.Bean.Film;
import com.example.mtimeapp.R;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private Context context;
    private List<Film> mlist;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView filmImage;
        TextView filmTitle;
        TextView filmSutitle;
        View FilmView;

        public ViewHolder(View view) {
            super(view);
            FilmView = view;
            filmTitle = (TextView)view.findViewById(R.id.film_title);       //标题
            filmImage = (ImageView)view.findViewById(R.id.film_image);              //图片
            filmSutitle = (TextView)view.findViewById(R.id.film_summary);         //副标题
        }
    }
    public FilmAdapter(Context context, List<Film> list) {
        this.context = context;
        this.mlist = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {                   //***************************
        return null;                                                                              //点击事件待补充
                                                                                                  //****************************
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Film film = mlist.get(i);
        Glide.with(context).load(film.getFilmImage()).into(viewHolder.filmImage);                  //展示图片，标题，发布时间。
        viewHolder.filmTitle.setText(film.getFilmTitle());
        viewHolder.filmSutitle.setText(film.getFilmSubTitle());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
