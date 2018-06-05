package com.shichuang.sendnar.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.ActionList;
import com.shichuang.sendnar.entify.Empty;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class PovertyAlleviationActivitiesAdapter extends BaseQuickAdapter<ActionList.ActionListModel, BaseViewHolder> {
    public PovertyAlleviationActivitiesAdapter() {
        super(R.layout.item_poverty_alleviation_activities);
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
    protected void convert(BaseViewHolder helper, ActionList.ActionListModel item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getShowPics()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_horizontal);
    }
}
