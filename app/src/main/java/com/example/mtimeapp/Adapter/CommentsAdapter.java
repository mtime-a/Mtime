package com.example.mtimeapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mtimeapp.CustomView.CircleImageView;
import com.example.mtimeapp.R;

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

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<Map<String, Object>> list;
    private String cookie;
    private String nickName;
    private String headImage;
    private String statu;

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
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder viewHolder, final int i) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        nickName = sharedPreferences.getString("theNickname", "");
        headImage = "http://132.232.78.106:8001/media/" + sharedPreferences.getString("theHeadImage", "");

        final Map map = list.get(i);
        Glide.with(context).load(map.get("autherHeadPhoto")).into(viewHolder.autherHeadPhoto);
        viewHolder.author.setText(map.get("author").toString());
        viewHolder.title.setText(map.get("content").toString());
        viewHolder.time.setText(map.get("Time").toString());
        if (nickName.equals(map.get("author").toString()) || headImage.equals(map.get("autherHeadPhoto"))) {
            viewHolder.delete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.delete.setVisibility(View.INVISIBLE);
        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickName.equals(map.get("author").toString()) || headImage.equals(map.get("autherHeadPhoto"))) {
                    if (map.get("type").toString().equals("news")) {
                        deleteComment(map.get("id").toString(), "1");
                    } else if (map.get("type").toString().equals("film")) {
                        deleteComment(map.get("id").toString(), "0");
                    }
                    list.remove(i);
                    notifyItemRemoved(i);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView author;
        TextView title;
        TextView time;
        ImageView delete;
        ImageView autherHeadPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.item_comment_name);
            title = itemView.findViewById(R.id.item_comment_content);
            time = itemView.findViewById(R.id.item_comment_time);
            delete = itemView.findViewById(R.id.deleteCommentOutside);
            autherHeadPhoto = itemView.findViewById(R.id.item_comment_icon);
        }
    }

    private void deleteComment(String id, String type) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        final FormBody formBody = new FormBody.Builder()
                .add("session", cookie)
                .add("id", id)
                .add("type", type)
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
                        Log.e("Adapter", msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
