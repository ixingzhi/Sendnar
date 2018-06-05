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
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.FestivalBannerAndGiftBag;

/**
 * Created by Administrator on 2018/4/25.
 */

public class GiftBagAdapter extends BaseQuickAdapter<FestivalBannerAndGiftBag.PackageList, BaseViewHolder> {
    public GiftBagAdapter() {
        super(R.layout.item_gift_bag);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FestivalBannerAndGiftBag.PackageList item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_horizontal);
        helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(item.getRealTotalPric(), 2));
        helper.setText(R.id.tv_gifts_name, item.getGiftPackName());

        // 微信送礼
        helper.getView(R.id.btn_wechat_gift_giving).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLogin(mContext)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("goodsId", item.getGoodsId());
                    bundle.putInt("buyType", BuyType.WECHAT_GIFT_GIVING);
                    RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle);
                }
            }
        });
        // 送给自己
        helper.getView(R.id.btn_direct_purchasing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isLogin(mContext)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("goodsId", item.getGoodsId());
                    bundle.putInt("buyType", BuyType.BUY_NOW);
                    RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle);
                }
            }
        });
    }
}
