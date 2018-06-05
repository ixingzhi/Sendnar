package com.shichuang.sendnar.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.MyItems;

/**
 * Created by Administrator on 2018/4/19.
 */

public class MyItemsAdapter extends BaseQuickAdapter<MyItems.MyItemsModel, BaseViewHolder> {
    public MyItemsAdapter() {
        super(R.layout.item_my_items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyItems.MyItemsModel item) {
        helper.getView(R.id.iv_select).setSelected(item.isSelect());
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getShopName());
        helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(item.getSalePrice(), 2));
    }
}
