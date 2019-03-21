package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.Bean.Show;
import com.example.mtimeapp.R;

import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private Context context;
    private List<Show> showList;

    public ShowAdapter(Context context, List<Show> list) {
        this.context = context;
        this.showList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //"num":"数量(int)",
//        "list":[{
//        "title":"电影标题",
//                "image":"缩略图",
//                "info":"简介",
//                "release_date":"上映时间",
//                "film_id":"电影ID",
//                "mark": "评分"
//    }],
//            "status": "ok"

        ImageView showImage;
        TextView showTitle;
        //  TextView showInfo;
        TextView showRelease_date;
        TextView showFilmId;
        TextView showMark;
        View ShowView;

        public ViewHolder(View view) {
            super(view);
            ShowView = view;
            showTitle = (TextView) view.findViewById(R.id.show_title);                  //标题
            showImage = (ImageView) view.findViewById(R.id.show_picture);              //图片
            //    showInfo = (TextView)view.findViewById(R.id.show);                  //副标题
            showRelease_date = (TextView) view.findViewById(R.id.show_date);
            showMark = (TextView) view.findViewById(R.id.show_score);

        }
    }

    @NonNull
    @Override
    public ShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {              //**************************************
        return null;                                                                                     //传值待补充
    }                                                                                                    //**************************************

    @Override
    public void onBindViewHolder(@NonNull ShowAdapter.ViewHolder viewHolder, int i) {
        Show show = showList.get(i);
        Glide.with(context).load(show.getShowImage()).into(viewHolder.showImage);
        viewHolder.showTitle.setText(show.getShowTitle());
        viewHolder.showRelease_date.setText(show.getShowRelease_time());
        viewHolder.showMark.setText(show.getShowMark());
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }
}
