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
        ImageView autImage;                          //作者头像
        TextView filmTitle;
        TextView filmInfo;
        TextView authorName;
        TextView authorId;
        TextView filmId;
        TextView commentNum;
        View FilmView;

        public ViewHolder(View view) {
            super(view);
            FilmView = view;
            filmTitle = (TextView)view.findViewById(R.id.item_film_title);                  //标题
            filmImage = (ImageView)view.findViewById(R.id.item_film_picture);              //图片
            filmInfo = (TextView)view.findViewById(R.id.item_film_info);                  //副标题
            autImage = (ImageView)view.findViewById(R.id.item_film_icon);
            authorName = (TextView)view.findViewById(R.id.item_author_name);
            authorId = (TextView)view.findViewById(R.id.item_film_id);
            commentNum = (TextView)view.findViewById(R.id.item_film_comment_num);

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
        Glide.with(context).load(film.getAut_head()).into(viewHolder.autImage);
        viewHolder.filmTitle.setText(film.getFilmTitle());
        viewHolder.filmInfo.setText(film.getFilmSubTitle());
        viewHolder.authorName.setText(film.getAut_name());
        viewHolder.authorId.setText(film.getAut_id());
        viewHolder.commentNum.setText(film.getComment_num());

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
