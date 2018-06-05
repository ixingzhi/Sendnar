package com.shichuang.sendnar.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.ConfirmOrderActivity;
import com.shichuang.sendnar.activity.GiftsDetailsActivity;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.GiftsDetailsType;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.GoodsList;

/**
 * Created by Administrator on 2018/5/3.
 */

public class RecommendGiftsAdapter extends BaseQuickAdapter<GoodsList.GoodsListModel, BaseViewHolder> {
    public RecommendGiftsAdapter() {
        super(R.layout.item_gifts_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, final GoodsList.GoodsListModel item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getShopName());
        helper.setText(R.id.tv_gifts_description, item.getLabels());
        helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(item.getSalePrice(), 2));
        helper.addOnClickListener(R.id.iv_add_shopping_cart);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                bundle.putInt("operationType", GiftsDetailsType.GOODS_DETAILS);
                RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle);
            }
        });
        // 微信送礼
        helper.getView(R.id.btn_wechat_gift_giving).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("goodsId", item.getId());
                bundle.putInt("buyType", BuyType.WECHAT_GIFT_GIVING);
                RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle);
            }
        });
        // 送给自己
        helper.getView(R.id.btn_direct_purchasing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("goodsId", item.getId());
                bundle.putInt("buyType", BuyType.BUY_NOW);
                RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle);
            }
        });
    }
}