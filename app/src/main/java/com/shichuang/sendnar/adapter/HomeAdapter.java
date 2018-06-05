package com.shichuang.sendnar.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.Home;


/**
 * Created by Administrator on 2018/4/17.
 */

public class HomeAdapter extends BaseQuickAdapter<Home.Activity, BaseViewHolder> {

    public HomeAdapter() {
        super(R.layout.item_home);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = super.onCreateDefViewHolder(parent, viewType);
        View view = viewHolder.itemView;
        ImageView ivPicture = view.findViewById(R.id.iv_picture);   // 图片比例   750 * 300
        int screenWidth = RxScreenTool.getDisplayMetrics(mContext).widthPixels;
        int height = (int) (((float) screenWidth * 300) / 750);
        ViewGroup.LayoutParams params = ivPicture.getLayoutParams();
        params.height = height;
        return viewHolder;
    }

    @Override
    protected void convert(BaseViewHolder helper, Home.Activity item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture));
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_describe, item.getLabel());
    }
}
