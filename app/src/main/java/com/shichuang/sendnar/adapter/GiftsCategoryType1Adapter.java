package com.shichuang.sendnar.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GiftsCategoryType1;


/**
 * Created by Administrator on 2018/4/17.
 */

public class GiftsCategoryType1Adapter extends BaseQuickAdapter<GiftsCategoryType1, BaseViewHolder> {

    public GiftsCategoryType1Adapter() {
        super(R.layout.item_gifts_category_type_1);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftsCategoryType1 item) {
        helper.setText(R.id.tv_title, item.getName());
        helper.setText(R.id.tv_describe, item.getDescribe());
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture),R.drawable.ic_avatar_default);
    }
}
