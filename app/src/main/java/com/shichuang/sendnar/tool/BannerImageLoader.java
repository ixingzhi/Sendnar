package com.shichuang.sendnar.tool;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shichuang.sendnar.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/11/30.
 */

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.ic_gift_default_horizontal);
        Glide.with(context).load(path).apply(options).into(imageView);
    }
}
