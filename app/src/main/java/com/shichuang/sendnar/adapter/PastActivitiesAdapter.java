package com.shichuang.sendnar.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.ActionList;
import com.shichuang.sendnar.entify.Empty;

/**
 * Created by Administrator on 2018/4/25.
 */

public class PastActivitiesAdapter extends BaseQuickAdapter<ActionList.ActionListModel, BaseViewHolder> {

    public PastActivitiesAdapter() {
        super(R.layout.item_past_activities);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionList.ActionListModel item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getShowPics()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_horizontal);
    }
}
