package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtimeapp.CustomView.RoundImageView;
import com.example.mtimeapp.FilmActivity;
import com.example.mtimeapp.NewsActivity;
import com.example.mtimeapp.R;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyCommentsAdapter extends RecyclerView.Adapter<MyCommentsAdapter.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> list;
    private String cookie;
    private String statu;
    int op = 0;

    public MyCommentsAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mycomment, viewGroup, false);
        MyCommentsAdapter.ViewHolder holder = new MyCommentsAdapter.ViewHolder(view);

        SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        Log.e("Adapter",cookie);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentsAdapter.ViewHolder viewHolder, final int i) {
        final Map map = list.get(i);

        String type = map.get("type").toString();
        if(type.equals("film")){
            viewHolder.time.setText(map.get("Time").toString());
  //        Glide.with(context).load(map.get("image")).into(viewHolder.image);
            viewHolder.content.setText(map.get("content").toString());
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, FilmActivity.class);
                    intent.putExtra("comment_id", map.get("filmId").toString());
                    context.startActivity(intent);
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("MyCommentsAdapter","删除news");
                    deleteComment(map.get("id").toString(),"0");
                    if(op == 1){
                        list.remove(i);
                        notifyItemRemoved(i);
                        notifyDataSetChanged();
                        op = 0;
                    }
                }
            });
        }
        if(type.equals("news")){
            viewHolder.time.setText(map.get("create_time").toString());
            //        Glide.with(context).load(map.get("image")).into(viewHolder.image);
            viewHolder.content.setText(map.get("content").toString());
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, NewsActivity.class);
                    intent.putExtra("id", map.get("newsId").toString());
                    context.startActivity(intent);
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("MyCommentsAdapter","删除news");
                    deleteComment(map.get("id").toString(),"1");
                    Log.e("MyCommentsAdapter",map.get("id").toString());
                    if(op == 1){
                    list.remove(i);
                    notifyItemRemoved(i);
                    notifyDataSetChanged();
                    op = 0;
                    }
                }
            });
        }
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
        TextView time;
        Button delete;
        TextView content;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_mycomment_image);
            time = itemView.findViewById(R.id.item_commentTime);
            content = itemView.findViewById(R.id.item_mycomment_content);
            layout = itemView.findViewById(R.id.item_mycomment_layout);
            delete = itemView.findViewById(R.id.deleteComment);
        }
    }
    private void deleteComment(String id, String type){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        final FormBody formBody = new FormBody.Builder()
                .add("session", cookie)
                .add("id",id)
                .add("type",type)
                .build();

        Log.e("Log", id + "/" + type + "/" + cookie);
        Request request = new Request.Builder()
                .url("http://132.232.78.106:8001/api/deleteMyNewsComment/")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "获取数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("Adapter", responseData);
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        statu = jsonObject.getString("state");
                        String msg = jsonObject.getString("msg");
                       // judgeState(statu);
                        Log.e("Adapter",msg);
                        op = 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
//    private void judgeState(String msg){
//        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
//    }
}
