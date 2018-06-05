package com.shichuang.sendnar.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.ConfirmOrder;
import com.shichuang.sendnar.entify.OrderDetails;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ConfirmOrderAdapter extends BaseQuickAdapter<ConfirmOrder.Goods, BaseViewHolder> {
    public ConfirmOrderAdapter() {
        super(R.layout.item_confirm_order_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConfirmOrder.Goods item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getShopName());
        helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(item.getSalePrice(), 2));
        helper.setText(R.id.tv_count, item.getCount() + "");
        helper.addOnClickListener(R.id.btn_subtract);
        helper.addOnClickListener(R.id.btn_plus);
    }
}
