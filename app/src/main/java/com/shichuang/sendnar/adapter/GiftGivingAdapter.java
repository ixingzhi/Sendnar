package com.shichuang.sendnar.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.GiftReceivedConfirmOrderActivity;
import com.shichuang.sendnar.activity.MyItemsActivity;
import com.shichuang.sendnar.common.GiftStatus;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.LookGiftInfo;

/**
 * Created by Administrator on 2018/4/19.
 */

public class GiftGivingAdapter extends BaseQuickAdapter<LookGiftInfo.LookGiftInfoModel, BaseViewHolder> {
    private int giftStatus;

    public GiftGivingAdapter() {
        super(R.layout.item_gift_giving);
    }

    @Override
    protected void convert(BaseViewHolder helper, LookGiftInfo.LookGiftInfoModel item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getShopName());
        helper.setText(R.id.tv_describe, item.getLabels());
        if (TextUtils.isEmpty(item.getReceiveTime())) {
            helper.setText(R.id.tv_time, "");
        } else {
            helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getReceiveTime()));
        }

        setOrderOperation(helper, item);
    }

    private void setOrderOperation(BaseViewHolder helper, final LookGiftInfo.LookGiftInfoModel item) {
        View mViewDiv = helper.getView(R.id.view_dividing);
        Button mBtnExchangeGift = helper.getView(R.id.btn_exchange_gift);
        Button mBtnGatherTogetherRedPacket = helper.getView(R.id.btn_gather_together_red_packet);
        Button mBtnExamplesOf = helper.getView(R.id.btn_examples_of);
        Button mBtnReceive = helper.getView(R.id.btn_receive);
        switch (giftStatus) {
            case GiftStatus.WAIT_RECEIVE:
                helper.setText(R.id.tv_receive_name, "该礼物未接收");
                helper.setText(R.id.tv_gift_status, "待接收");
                mViewDiv.setVisibility(View.GONE);
                mBtnExchangeGift.setVisibility(View.GONE);
                mBtnGatherTogetherRedPacket.setVisibility(View.GONE);
                mBtnExamplesOf.setVisibility(View.GONE);
                mBtnReceive.setVisibility(View.GONE);
                break;
            case GiftStatus.HAS_RECEIVE:
                helper.setText(R.id.tv_receive_name, "送给-" + item.getNickname() + "的礼物");
                helper.setText(R.id.tv_gift_status, "已接收");
                mViewDiv.setVisibility(View.VISIBLE);
                mBtnExchangeGift.setVisibility(View.GONE);
                mBtnGatherTogetherRedPacket.setVisibility(View.GONE);
                mBtnExamplesOf.setVisibility(View.GONE);
                mBtnReceive.setVisibility(View.GONE);
                break;
            case GiftStatus.TIMEOUT:
                helper.setText(R.id.tv_receive_name, "该礼物已超时");
                helper.setText(R.id.tv_gift_status, "已超时");
                mViewDiv.setVisibility(View.VISIBLE);
                mBtnExchangeGift.setVisibility(View.VISIBLE);
                mBtnGatherTogetherRedPacket.setVisibility(View.VISIBLE);
                mBtnExamplesOf.setVisibility(View.VISIBLE);
                mBtnReceive.setVisibility(View.VISIBLE);
                break;
            default:
                helper.setText(R.id.tv_receive_name, "");
                mViewDiv.setVisibility(View.GONE);
                mBtnExchangeGift.setVisibility(View.GONE);
                mBtnGatherTogetherRedPacket.setVisibility(View.GONE);
                mBtnExamplesOf.setVisibility(View.GONE);
                mBtnReceive.setVisibility(View.GONE);
                break;
        }
        helper.addOnClickListener(R.id.btn_gather_together_red_packet);
        helper.addOnClickListener(R.id.btn_examples_of);
        helper.addOnClickListener(R.id.btn_receive);
    }

    public void setGiftStatus(int status) {
        this.giftStatus = status;
    }
}
