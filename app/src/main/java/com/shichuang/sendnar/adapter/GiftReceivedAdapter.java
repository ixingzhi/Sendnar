package com.shichuang.sendnar.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.ClassifyActivity;
import com.shichuang.sendnar.common.GiftStatus;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.entify.LookGiftInfo;

/**
 * Created by Administrator on 2018/4/19.
 */

public class GiftReceivedAdapter extends BaseQuickAdapter<LookGiftInfo.LookGiftInfoModel, BaseViewHolder> {
    private int giftStatus;

    public GiftReceivedAdapter() {
        super(R.layout.item_gift_received);
    }

    @Override
    protected void convert(BaseViewHolder helper, LookGiftInfo.LookGiftInfoModel item) {
        helper.setText(R.id.tv_receive_name, "来自-" + item.getNickname() + "的礼物");
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getShopName());
        helper.setText(R.id.tv_gifts_price, "¥ " + RxBigDecimalTool.toDecimal(item.getSalePrice(), 2));

        setOrderOperation(helper, item);
    }

    private void setOrderOperation(BaseViewHolder helper, LookGiftInfo.LookGiftInfoModel item) {
        View mViewDiv = helper.getView(R.id.view_dividing);
        Button mBtnExchangeGift = helper.getView(R.id.btn_exchange_gift);
        Button mBtnGatherTogetherRedPacket = helper.getView(R.id.btn_gather_together_red_packet);
        Button mBtnExamplesOf = helper.getView(R.id.btn_examples_of);
        Button mBtnReceive = helper.getView(R.id.btn_receive);
        Button mBtnExamplesDetails = helper.getView(R.id.btn_examples_details);
        switch (giftStatus) {
            case GiftStatus.HAS_RECEIVE:
                helper.setText(R.id.tv_gift_status, "待处理");
                mViewDiv.setVisibility(View.VISIBLE);
                mBtnExchangeGift.setVisibility(View.VISIBLE);
                mBtnGatherTogetherRedPacket.setVisibility(View.VISIBLE);
                mBtnExamplesOf.setVisibility(View.VISIBLE);
                mBtnReceive.setVisibility(View.VISIBLE);
                mBtnExamplesDetails.setVisibility(View.GONE);
                break;
            case GiftStatus.HAS_EXAMPLES:
                helper.setText(R.id.tv_gift_status, "已转赠");
                mViewDiv.setVisibility(View.GONE);
                mBtnExchangeGift.setVisibility(View.GONE);
                mBtnGatherTogetherRedPacket.setVisibility(View.GONE);
                mBtnExamplesOf.setVisibility(View.GONE);
                mBtnReceive.setVisibility(View.GONE);
                mBtnExamplesDetails.setVisibility(View.GONE);
                break;
            default:
                mViewDiv.setVisibility(View.GONE);
                mBtnExchangeGift.setVisibility(View.GONE);
                mBtnGatherTogetherRedPacket.setVisibility(View.GONE);
                mBtnExamplesOf.setVisibility(View.GONE);
                mBtnReceive.setVisibility(View.GONE);
                mBtnExamplesDetails.setVisibility(View.GONE);
                break;
        }

        helper.addOnClickListener(R.id.btn_exchange_gift);
        helper.addOnClickListener(R.id.btn_gather_together_red_packet);
        helper.addOnClickListener(R.id.btn_examples_of);
        helper.addOnClickListener(R.id.btn_receive);
    }

    public void setGiftStatus(int status) {
        this.giftStatus = status;
    }
}
