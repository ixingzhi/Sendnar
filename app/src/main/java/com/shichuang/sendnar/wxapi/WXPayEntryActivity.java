package com.shichuang.sendnar.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.shichuang.sendnar.widget.payment.wechat.pay.WechatPay;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WechatPay.getInstance().getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        WechatPay.getInstance().onResp(baseResp.errCode);
        finish();
    }
}