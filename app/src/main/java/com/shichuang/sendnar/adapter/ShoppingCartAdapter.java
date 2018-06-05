package com.shichuang.sendnar.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.ShoppingCart;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ShoppingCartAdapter extends BaseQuickAdapter<ShoppingCart, BaseViewHolder> {

    public ShoppingCartAdapter() {
        super(R.layout.item_shopping_cart);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ShoppingCart item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getShopName());
        helper.setText(R.id.tv_count, item.getGoodsCartCounts() + "");
        helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(item.getSalePrice(), 2));

        helper.addOnClickListener(R.id.iv_select);
        helper.addOnClickListener(R.id.tv_subtract);
        helper.addOnClickListener(R.id.tv_plus);
        helper.addOnClickListener(R.id.right);
        helper.getView(R.id.iv_select).setSelected(item.isSelect());
    }
}

