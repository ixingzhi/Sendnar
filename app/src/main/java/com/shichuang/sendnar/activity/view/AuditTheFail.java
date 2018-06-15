package com.shichuang.sendnar.activity.view;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.GoodsAfterAuditActivity;
import com.shichuang.sendnar.common.UdeskHelper;
import com.shichuang.sendnar.entify.RefundInfo;

/**
 * 审核结果
 * Created by Administrator on 2018/6/13.
 */

public class AuditTheFail {
    private View view;
    private TextView mTvApplyType;
    private TextView mTvApplyTime;
    private TextView mTvTypeInstructionsTitle;
    private TextView mTvTypeInstructions;
    private TextView mTvRefusedTime;
    private TextView mTvHandleReason;


    public AuditTheFail(final GoodsAfterAuditActivity activity) {
        view = LayoutInflater.from(activity).inflate(R.layout.layout_audit_the_fail, (ViewGroup) activity.findViewById(R.id.content), false);
        // 默认影藏
        view.setVisibility(View.GONE);
        mTvApplyType = view.findViewById(R.id.tv_apply_type);
        mTvApplyTime = view.findViewById(R.id.tv_apply_time);
        mTvTypeInstructionsTitle = view.findViewById(R.id.tv_type_instructions_title);
        mTvTypeInstructions = view.findViewById(R.id.tv_type_instructions);
        mTvRefusedTime = view.findViewById(R.id.tv_refused_time);
        mTvHandleReason = view.findViewById(R.id.tv_handle_reason);

        view.findViewById(R.id.btn_contact_customer_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UdeskHelper.entryChat(activity);
            }
        });
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
        mTvHandleReason.setText(data.getHandleRemark());

        mTvRefusedTime.setText(TextUtils.isEmpty(data.getRefuseRefundTime()) ? "" : RxTimeTool.stringFormat(data.getRefuseRefundTime()));
        mTvHandleReason.setText(data.getHandleRemark());
    }

    public void onDestroy() {
        view = null;
    }
}