package com.shichuang.sendnar.activity.view;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.GoodsAfterAuditActivity;
import com.shichuang.sendnar.entify.RefundInfo;

/**
 * 等待审核
 * Created by Administrator on 2018/6/12.
 */

public class WaitingForAudit {
    private View view;
    private TextView mTvApplyType;
    private TextView mTvApplyTime;
    private TextView mTvTypeInstructionsTitle;
    private TextView mTvTypeInstructions;
    private TextView mTvTypeAmountTitle;
    private TextView mTvTypeAmount;

    public WaitingForAudit(GoodsAfterAuditActivity activity) {
        view = LayoutInflater.from(activity).inflate(R.layout.layout_waiting_for_audit, (ViewGroup) activity.findViewById(R.id.content), false);
        // 默认影藏
        view.setVisibility(View.GONE);
        mTvApplyType = view.findViewById(R.id.tv_apply_type);
        mTvApplyTime = view.findViewById(R.id.tv_apply_time);
        mTvTypeInstructionsTitle = view.findViewById(R.id.tv_type_instructions_title);
        mTvTypeInstructions = view.findViewById(R.id.tv_type_instructions);
        mTvTypeAmountTitle = view.findViewById(R.id.tv_type_amount_title);
        mTvTypeAmount = view.findViewById(R.id.tv_type_amount);
    }

    public View getView() {
        return view;
    }

    public void setData(String type, RefundInfo data) {
        mTvApplyType.setText("买家申请" + type);
        // 申请时间
        if (!TextUtils.isEmpty(data.getBuyerSubmitTime())) {
            mTvApplyTime.setText(RxTimeTool.stringFormat(data.getBuyerSubmitTime()));
        } else {
            mTvApplyTime.setText("");
        }
        mTvTypeInstructionsTitle.setText(type + "说明");
        mTvTypeInstructions.setText(data.getReturnReason());
        mTvTypeAmountTitle.setText(type + "金额");
        if (!TextUtils.isEmpty(data.getRefundAmount())) {
            mTvTypeAmount.setText(RxBigDecimalTool.toDecimal(data.getRefundAmount(), 2) + "元");
        }
    }

    public void onDestroy() {
        view = null;
    }

}
