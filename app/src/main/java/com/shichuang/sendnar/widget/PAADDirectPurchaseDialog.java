package com.shichuang.sendnar.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.ConfirmOrderActivity;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.GiftsDetails;
import com.shichuang.sendnar.entify.PovertyAlleviationActivitiesDetails;


/**
 * 扶贫详情送给自己Dialog
 * Created by Administrator on 2018/4/18.
 */

public class PAADDirectPurchaseDialog extends BaseDialog implements View.OnClickListener {
    private View view;
    private ImageView mIvPicture;
    private TextView mTvGiftsPrice;
    private TextView mTvCount;
    // 默认数量
    private int count = 1;
    private PovertyAlleviationActivitiesDetails giftsDetails;

    public PAADDirectPurchaseDialog(Context context) {
        super(context, 0.6f, Gravity.BOTTOM);
        setFullScreenWidth();
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_direct_purchase, null);
        setContentView(view);
        initView();
        initEvent();
    }

    private void initView() {
        mIvPicture = view.findViewById(R.id.iv_picture);
        mTvGiftsPrice = view.findViewById(R.id.tv_gifts_price);
        mTvCount = view.findViewById(R.id.tv_count);
    }

    private void initEvent() {
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm_order).setOnClickListener(this);
        view.findViewById(R.id.tv_subtract).setOnClickListener(this);
        view.findViewById(R.id.tv_plus).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_confirm_order:
                if (giftsDetails != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("goodsId", giftsDetails.getActionItem().getGoodsId());
                    bundle.putInt("count", count);
                    bundle.putInt("buyType", BuyType.BUY_NOW);
                    RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle);
                } else {
                    RxToastTool.showShort("订单信息有误~");
                }
                dismiss();
                break;
            // 减
            case R.id.tv_subtract:
                count--;
                if (count < 1) {
                    count = 1;
                    RxToastTool.showShort("不能再减啦~");
                }
                mTvCount.setText(String.valueOf(count));
                break;
            // 加
            case R.id.tv_plus:
                count++;
                mTvCount.setText(String.valueOf(count));
                break;
            default:
                break;
        }
    }

    public void setGiftInfo(PovertyAlleviationActivitiesDetails data) {
        if (data == null)
            return;
        this.giftsDetails = data;
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(data.getActionDetail().getPic()), mIvPicture, R.drawable.ic_gift_default_square);
        mTvGiftsPrice.setText("¥：" + RxBigDecimalTool.toDecimal(giftsDetails.getActionItem().getPromotionPrice(), 2));
    }
}
