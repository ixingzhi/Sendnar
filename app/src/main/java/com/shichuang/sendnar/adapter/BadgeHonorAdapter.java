package com.shichuang.sendnar.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.HonoraryContribution;

/**
 * Created by Administrator on 2018/5/21.
 */

public class BadgeHonorAdapter extends BaseQuickAdapter<HonoraryContribution.Badge, BaseViewHolder> {

    public BadgeHonorAdapter() {
        super(R.layout.item_badge_honor);
    }

    @Override
    protected void convert(BaseViewHolder helper, HonoraryContribution.Badge item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture));
    }
}
