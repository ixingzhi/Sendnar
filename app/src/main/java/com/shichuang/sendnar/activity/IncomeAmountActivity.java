package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.event.FinishActivityEvent;
import com.shichuang.sendnar.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 收益金额
 * Created by Administrator on 2018/4/28.
 */

public class IncomeAmountActivity extends BaseActivity {
    private String withdrawalAmount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_income_amount;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        withdrawalAmount = getIntent().getStringExtra("withdrawalAmount");
        if (!TextUtils.isEmpty(withdrawalAmount)) {
            ((TextView) findViewById(R.id.tv_withdrawal_amount)).setText("¥" + RxBigDecimalTool.toDecimal(withdrawalAmount, 2));
        }
        EventBus.getDefault().register(mContext);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0.00".equals(RxBigDecimalTool.toDecimal(withdrawalAmount, 2))) {
                    showToast("没有可提现的金额");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("withdrawalAmount", withdrawalAmount);
                    RxActivityTool.skipActivity(mContext, CommissionWithdrawalActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void initData() {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinishActivityEvent event) {
        if (null != event) {
            if (!this.isFinishing()) {
                this.finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }
}
