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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.FilmActivity;
import com.example.mtimeapp.NewsActivity;
import com.example.mtimeapp.R;

import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Map<String, Object>> list;
    private final static int ITEM_CONTENT = 0;
    private final static int ITEM_FOOT = 1;
    private boolean hasMore;

    public NewsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_CONTENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
            return new ViewHolder(view);
        }
        if (viewType==ITEM_FOOT){
            View view = LayoutInflater.from(context).inflate(R.layout.footview, viewGroup, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof ViewHolder){
            Map map = list.get(position);
            ((ViewHolder) viewHolder).title.setText(map.get("Title").toString());
            //((ViewHolder) holder).tvText.setOnClickListener(view -> Toast.makeText(mContext, mData.get(position-mHeader), Toast.LENGTH_SHORT).show());
            Glide.with(context).load(map.get("photo")).into(((ViewHolder)viewHolder).picture);
            ((ViewHolder) viewHolder).date.setText(map.get("Time").toString());
            ((ViewHolder) viewHolder).author.setText(map.get("author").toString());
            ((ViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, NewsActivity.class);
                    intent.putExtra("id", list.get(position).get("id").toString());
                    context.startActivity(intent);
                }
            });
        }
        if (viewHolder instanceof FooterViewHolder) {
            ((FooterViewHolder) viewHolder).remind.setVisibility(View.INVISIBLE);
            //((FooterViewHolder) viewHolder).remind.setText("正在加载...");
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= list.size()){
            return ITEM_FOOT;
        }
        return ITEM_CONTENT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView title;
        TextView date;
        CardView cardView;
        TextView author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.item_news_card);
            title = itemView.findViewById(R.id.item_news_title);
            picture = itemView.findViewById(R.id.item_news_picture);
            date = itemView.findViewById(R.id.item_news_date);
            author = itemView.findViewById(R.id.item_news_author);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView remind;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            remind = itemView.findViewById(R.id.tips);
        }
    }

}



