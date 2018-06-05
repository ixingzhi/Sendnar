package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.ApplyWithdrawals;
import com.shichuang.sendnar.event.FinishActivityEvent;
import com.shichuang.sendnar.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/4/28.
 */

public class WithdrawalSuccessActivity extends BaseActivity {
    private ApplyWithdrawals withdrawalSuccess;

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdrawal_success;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        withdrawalSuccess = (ApplyWithdrawals) getIntent().getSerializableExtra("withdrawalSuccess");
        if (withdrawalSuccess != null) {
            ((TextView) findViewById(R.id.tv_withdrawal_amount)).setText("¥" + RxBigDecimalTool.toDecimal(withdrawalSuccess.getWithdrawAmount(), 2));
            ((TextView) findViewById(R.id.tv_service_fee_and_tax_rate)).setText("¥" + RxBigDecimalTool.toDecimal(withdrawalSuccess.getServiceCharge(), 2));
            // 关闭提现流程页面
            EventBus.getDefault().post(new FinishActivityEvent());
            // 更新我的团队页面数据
            EventBus.getDefault().post(new MessageEvent("withdrawalSuccess"));
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
    }

    @Override
    public void initData() {
    }
}
