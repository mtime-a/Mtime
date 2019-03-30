package com.example.mtimeapp.Util;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.example.mtimeapp.NewsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class RichText {

    private Context context;
    private TextView textView;
    private String body;

    public RichText(Context context, TextView textView, String body) {
        this.context = context;
        this.textView = textView;
        this.body = body;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        MyTagHandler myTagHandler = new MyTagHandler(context);
        textView.setText(Html.fromHtml(body, new URLImageParser(textView, context), myTagHandler));
    }


}
