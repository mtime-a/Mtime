package com.example.mtimeapp.Util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class URLImageParser implements Html.ImageGetter {
    TextView mTextView;
    Context context;


    public URLImageParser(TextView textView, Context context) {
        this.mTextView = textView;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
//        Log.d("ChapterActivity", source);
        ImageLoader.getInstance().loadImage(source, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Bitmap resultBitMap = zoomBitMap(loadedImage);

                Bitmap resultBitMap = loadedImage;

                urlDrawable.bitmap = resultBitMap;

                urlDrawable.setBounds((mTextView.getWidth() - resultBitMap.getWidth()) / 2, 0, (mTextView.getWidth() - resultBitMap.getWidth()) / 2 + resultBitMap.getWidth(), resultBitMap.getHeight());

                mTextView.invalidate();
                mTextView.setText(mTextView.getText()); // 解决图文重叠
            }
        });

        return urlDrawable;

    }

    /**
     * 完成适配
     * 缩放 bitmap,由于还要考虑到放大倍数越大清晰度越低 和 图片太大显示不完整 的问题,还需要做详细的设计
     *
     * @param bitmap
     * @return
     */
    public Bitmap zoomBitMap(Bitmap bitmap) {

        float withTV = mTextView.getWidth();
        float withBM = bitmap.getWidth();
        Log.e("aaa", "withTV:" + withTV + "--withBM:" + withBM);
        float zoomNumb = withTV / withBM;
        Log.e("aaa", "zoomNumb:" + zoomNumb);

        Matrix matrix = new Matrix();

        matrix.postScale(zoomNumb, zoomNumb); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}

