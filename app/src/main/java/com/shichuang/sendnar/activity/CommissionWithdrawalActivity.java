package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ApplyWithdrawals;
import com.shichuang.sendnar.entify.AuthorizationOpenId;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.Page;
import com.shichuang.sendnar.entify.TaxRate;
import com.shichuang.sendnar.event.FinishActivityEvent;
import com.shichuang.sendnar.widget.PromptDialog;
import com.shichuang.sendnar.widget.RxTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/4/28.
 */

public class CommissionWithdrawalActivity extends BaseActivity {
    private EditText mEtWithdrawalAmount;
    private TextView mTvTaxRateAmount;

    private String withdrawalAmount;

    private double rate = 0.00;

    @Override
    public int getLayoutId() {
        return R.layout.activity_commission_withdrawal;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        withdrawalAmount = getIntent().getStringExtra("withdrawalAmount");
        mEtWithdrawalAmount = (EditText) findViewById(R.id.et_withdrawal_amount);
        mTvTaxRateAmount = (TextView) findViewById(R.id.tv_tax_rate_amount);
        EventBus.getDefault().register(mContext);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
        findViewById(R.id.tv_look_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tax_calculation, null);
                TextView mTvTaxRate = view.findViewById(R.id.tv_tax_rate);
                TextView mTvTaxTateAmount = view.findViewById(R.id.tv_tax_rate_amount);
                TextView mTvTaxRateExample = view.findViewById(R.id.tv_tax_rate_example);
                mTvTaxRate.setText(String.format("代扣个人所得税及相关手续费：%s", (rate * 100) + "%"));
                String amount = "";
                String amountTotal = "0.00";
                if (!TextUtils.isEmpty(mEtWithdrawalAmount.getText())) {
                    amount = RxBigDecimalTool.toDecimal(mEtWithdrawalAmount.getText().toString(), 2);
                    amountTotal = RxBigDecimalTool.toDecimal(Double.parseDouble(amount) * rate, 2);
                }
                mTvTaxRateExample.setText(amount + "元的个税计算");
                mTvTaxTateAmount.setText(amount + "*" + (rate * 100) + "%=" + amountTotal + "元");
                PromptDialog mDialog = new PromptDialog(mContext, view);
                mDialog.show();
            }
        });
        mEtWithdrawalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mEtWithdrawalAmount.setText(s);
                        mEtWithdrawalAmount.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mEtWithdrawalAmount.setText(s);
                    mEtWithdrawalAmount.setSelection(2);
                }
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mEtWithdrawalAmount.setText(s.subSequence(0, 1));
                        mEtWithdrawalAmount.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String amount = mEtWithdrawalAmount.getText().toString();
                Log.d("test", s.toString());
                if (amount.length() > 0) {
                    double rateAmount = RxBigDecimalTool.mul(Double.valueOf(amount), rate);
                    mTvTaxRateAmount.setText("此次提现扣除手续费和税金 合计¥" + RxBigDecimalTool.toDecimal(rateAmount, 2));
                } else {
                    mTvTaxRateAmount.setText("此次提现扣除手续费和税金 合计¥0.00");
                }
            }
        });
    }


    @Override
    public void initData() {
        getTaxRate();
    }

    /**
     * 获取提现税率
     */
    private void getTaxRate() {
        OkGo.<AMBaseDto<TaxRate>>get(Constants.getTaxRateUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<TaxRate>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<TaxRate>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<TaxRate>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            rate = response.body().data.getRate();
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<TaxRate>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    private void checkInfo() {
        String amount = mEtWithdrawalAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            showToast("请输入提现金额");
        } else if (Double.valueOf(amount) < 2) {
            showToast("提现金额不能少于2元");
        } else if (Double.valueOf(amount) > Double.valueOf(withdrawalAmount)) {
            showToast("超出最大可提现金额");
        } else {
            applyWithdrawals(amount);
        }
    }

    private void applyWithdrawals(final String amount) {
        OkGo.<AMBaseDto<ApplyWithdrawals>>post(Constants.applyWithdrawUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("withdrawAmount", amount)
                .execute(new NewsCallback<AMBaseDto<ApplyWithdrawals>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ApplyWithdrawals>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<ApplyWithdrawals>> response) {
                        showToast(response.body().msg);
                        dismissLoading();
                        if (response.body().code == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("withdrawalSuccess", response.body().data);
                            RxActivityTool.skipActivity(mContext, WithdrawalSuccessActivity.class, bundle);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ApplyWithdrawals>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
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
