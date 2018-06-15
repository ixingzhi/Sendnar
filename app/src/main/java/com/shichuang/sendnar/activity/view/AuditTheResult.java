package com.shichuang.sendnar.activity.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.GoodsAfterAuditActivity;
import com.shichuang.sendnar.activity.LogisticsStatusActivity;
import com.shichuang.sendnar.entify.RefundInfo;

/**
 * 审核结果
 * Created by Administrator on 2018/6/13.
 */

public class AuditTheResult {
    private View view;
    private TextView mTvApplyType;
    private TextView mTvApplyTime;
    private TextView mTvTypeInstructionsTitle;
    private TextView mTvTypeInstructions;
    // 处理结果
    private TextView mTvHandleTime;
    private LinearLayout mLlHandleResult;
    private TextView mTvHandleResult;
    private TextView mTvHandleResultInstructions;
    // 买家发货
    private TextView mTvBuyersDeliveryTime;
    private LinearLayout mLlBuyersDeliveryInfo;
    private TextView mTvLogisticsOrder;
    private TextView mTvLogisticsCompany;
    // 平台已收货
    private TextView mTvPlatformTakeGoodsTime;
    private LinearLayout mLlPlatformTakeGoods;
    // 平台发货信息
    private TextView mTvPlatformDeliveryTime;
    private LinearLayout mLlPlatformDeliveryInfo;
    private TextView mTvPlatformLogisticsOrder;
    private TextView mTvPlatformLogisticsCompany;
    // 买家收货
    private TextView mTvBuyerTimeOfReceipt;
    private LinearLayout mLlBuyerTimeOfReceipt;
    private ImageView mIvExchangeGoodsSuccess;
    private TextView mTvExchangeGoodsSuccess;

    private RefundInfo data;

    public AuditTheResult(final GoodsAfterAuditActivity activity) {
        view = LayoutInflater.from(activity).inflate(R.layout.layout_audit_the_result, (ViewGroup) activity.findViewById(R.id.content), false);
        // 默认影藏
        view.setVisibility(View.GONE);
        mTvApplyType = view.findViewById(R.id.tv_apply_type);
        mTvApplyTime = view.findViewById(R.id.tv_apply_time);
        mTvTypeInstructionsTitle = view.findViewById(R.id.tv_type_instructions_title);
        mTvTypeInstructions = view.findViewById(R.id.tv_type_instructions);

        mTvHandleTime = view.findViewById(R.id.tv_handle_time);
        mLlHandleResult = view.findViewById(R.id.ll_handle_result);
        mTvHandleResult = view.findViewById(R.id.tv_handle_result);
        mTvHandleResultInstructions = view.findViewById(R.id.tv_handle_result_instructions);

        mTvBuyersDeliveryTime = view.findViewById(R.id.tv_buyers_delivery_time);
        mLlBuyersDeliveryInfo = view.findViewById(R.id.ll_buyers_delivery_info);
        mTvLogisticsOrder = view.findViewById(R.id.tv_logistics_order);
        mTvLogisticsCompany = view.findViewById(R.id.tv_logistics_company);

        mTvPlatformTakeGoodsTime = view.findViewById(R.id.tv_platform_take_goods_time);
        mLlPlatformTakeGoods = view.findViewById(R.id.ll_platform_take_goods);

        mTvPlatformDeliveryTime = view.findViewById(R.id.tv_platform_delivery_time);
        mLlPlatformDeliveryInfo = view.findViewById(R.id.ll_platform_delivery_info);
        mTvPlatformLogisticsOrder = view.findViewById(R.id.tv_platform_logistics_order);
        mTvPlatformLogisticsCompany = view.findViewById(R.id.tv_platform_logistics_company);

        mTvBuyerTimeOfReceipt = view.findViewById(R.id.tv_buyer_time_of_receipt);
        mLlBuyerTimeOfReceipt = view.findViewById(R.id.ll_buyer_time_of_receipt);
        mIvExchangeGoodsSuccess = view.findViewById(R.id.iv_exchange_goods_success);
        mTvExchangeGoodsSuccess = view.findViewById(R.id.tv_exchange_goods_success);

        // 买家发货物流查询
        mTvBuyersDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("logisticsNo", data.getExpressNo());
                bundle.putString("logisticsCompany", data.getCom());
                RxActivityTool.skipActivity(activity, LogisticsStatusActivity.class, bundle);
            }
        });
        // 平台发货物流查询
        mTvBuyersDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("logisticsNo", data.getPlatformExpressNo());
                bundle.putString("logisticsCompany", data.getPlatformCom());
                RxActivityTool.skipActivity(activity, LogisticsStatusActivity.class, bundle);
            }
        });
    }

    public View getView() {
        return view;
    }

    public void setData(String type, RefundInfo data) {
        this.data = data;

        mTvApplyType.setText("买家申请" + type);
        // 申请时间
        if (!TextUtils.isEmpty(data.getBuyerSubmitTime())) {
            mTvApplyTime.setText(RxTimeTool.stringFormat(data.getBuyerSubmitTime()));
        } else {
            mTvApplyTime.setText("");
        }
        mTvTypeInstructionsTitle.setText(type + "说明");
        mTvTypeInstructions.setText(data.getReturnReason());

        mTvHandleTime.setText(TextUtils.isEmpty(data.getAgreeRefundTime()) ? "" : RxTimeTool.stringFormat(data.getAgreeRefundTime()));
        if (data.getStatus() == 2) {
            mTvHandleResult.setText("平台已拒绝您的" + type);
        } else {
            mTvHandleResult.setText("平台已同意您的" + type);
        }
        mTvHandleResultInstructions.setText(data.getHandleRemark());
        mTvBuyersDeliveryTime.setText(TextUtils.isEmpty(data.getReturnedTime()) ? "" : RxTimeTool.stringFormat(data.getReturnedTime()));
        mTvLogisticsOrder.setText("物流订单：" + data.getExpressNo());
        mTvLogisticsCompany.setText("物流公司：" + data.getExpressCompany());

        // 待卖家处理=1|卖家拒绝退款/退货/换货=2|待买家退货=3|买家已退货,等待卖家确认收货=4|卖家已退款=5|待财务打款=6|待卖家发货=7|已发货=8|买家取消=9|买家换货确认收货=10
        switch (data.getStatus()) {
            case 1:  // 待卖家处理=1
                mLlHandleResult.setVisibility(View.GONE);   // 处理结果
                mLlBuyersDeliveryInfo.setVisibility(View.GONE);  // 买家发货
                mLlPlatformTakeGoods.setVisibility(View.GONE);  // 平台已收货
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);  // 平台发货信息
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);  //   买家收货
                break;
            case 2:   // 卖家拒绝退款/退货/换货=2
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.GONE);
                mLlPlatformTakeGoods.setVisibility(View.GONE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);

                break;
            case 3:   // 待买家退货=3
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.GONE);
                mLlPlatformTakeGoods.setVisibility(View.GONE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);

                break;
            case 4:   // 买家已退货,等待卖家确认收货=4
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.VISIBLE);
                mLlPlatformTakeGoods.setVisibility(View.GONE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);

                break;
            case 5:   // 卖家已退款=5
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.VISIBLE);
                mLlPlatformTakeGoods.setVisibility(View.VISIBLE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);

                break;
            case 6:   // 待财务打款=6
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.GONE);
                mLlPlatformTakeGoods.setVisibility(View.GONE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);

                break;
            case 7:   // 待卖家发货=7
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.VISIBLE);
                mLlPlatformTakeGoods.setVisibility(View.VISIBLE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);


                break;
            case 8:   // 已发货=8
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.VISIBLE);
                mLlPlatformTakeGoods.setVisibility(View.VISIBLE);
                mLlPlatformDeliveryInfo.setVisibility(View.VISIBLE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);

                break;
            case 9:    // 买家取消=9
                mLlHandleResult.setVisibility(View.GONE);
                mLlBuyersDeliveryInfo.setVisibility(View.GONE);
                mLlPlatformTakeGoods.setVisibility(View.GONE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);
                break;
            case 10:  // 买家换货确认收货=10
                mLlHandleResult.setVisibility(View.VISIBLE);
                mLlBuyersDeliveryInfo.setVisibility(View.VISIBLE);
                mLlPlatformTakeGoods.setVisibility(View.VISIBLE);
                mLlPlatformDeliveryInfo.setVisibility(View.VISIBLE);
                mLlBuyerTimeOfReceipt.setVisibility(View.VISIBLE);

                break;

            default:
                mLlHandleResult.setVisibility(View.GONE);
                mLlBuyersDeliveryInfo.setVisibility(View.GONE);
                mLlPlatformTakeGoods.setVisibility(View.GONE);
                mLlPlatformDeliveryInfo.setVisibility(View.GONE);
                mLlBuyerTimeOfReceipt.setVisibility(View.GONE);
                break;
        }

    }

    public void onDestroy() {
        view = null;
    }
}
