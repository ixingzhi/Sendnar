package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.view.AuditTheFail;
import com.shichuang.sendnar.activity.view.AuditTheResult;
import com.shichuang.sendnar.activity.view.WaitingForAudit;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.RefundInfo;
import com.shichuang.sendnar.widget.RxTitleBar;

/**
 * 商品售后审核
 * Created by Administrator on 2018/6/5.
 */

public class GoodsAfterAuditActivity extends BaseActivity {
    private RxTitleBar mTitleBar;
    private FrameLayout mFlContent;
    // 等待审核
    private WaitingForAudit mWaitingForAudit;
    private AuditTheResult mAuditTheResult;
    private AuditTheFail mAuditTheFail;

    private int orderDetailId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_after_audit;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderDetailId = getIntent().getIntExtra("orderDetailId", 0);
        mTitleBar = view.findViewById(R.id.title_bar);
        mFlContent = view.findViewById(R.id.fl_content);

        mWaitingForAudit = new WaitingForAudit(this);
        mFlContent.addView(mWaitingForAudit.getView());

        mAuditTheResult = new AuditTheResult(this);
        mFlContent.addView(mAuditTheResult.getView());

        mAuditTheFail = new AuditTheFail(this);
        mFlContent.addView(mAuditTheFail.getView());
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        getAuditData();
    }

    private void getAuditData() {
        OkGo.<AMBaseDto<RefundInfo>>get(Constants.refundInfoUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("order_detail_id", orderDetailId)
                .execute(new NewsCallback<AMBaseDto<RefundInfo>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<RefundInfo>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<RefundInfo>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<RefundInfo>> response) {
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

    private void handleData(RefundInfo data) {
        // 是否需要退货(我需要退货=1|不需要退货=2|换货=3)
        String type = "";
        if (data.getNeedReturn() == 1) {
            type = "退货";
        } else if (data.getNeedReturn() == 2) {
            type = "退款";
        } else if (data.getNeedReturn() == 3) {
            type = "换货";
        }


        // 待卖家处理=1|卖家拒绝退款/退货/换货=2|待买家退货=3|买家已退货,等待卖家确认收货=4|卖家已退款=5|待财务打款=6|待卖家发货=7|已发货=8|买家取消=9|买家换货确认收货=10
        if (data.getStatus() == 1) {
            mWaitingForAudit.setData(type, data);
            mTitleBar.setTitle("等待审核");
            mWaitingForAudit.getView().setVisibility(View.VISIBLE);
            mAuditTheFail.getView().setVisibility(View.GONE);
            mAuditTheResult.getView().setVisibility(View.GONE);
        } else if (data.getStatus() == 2) {
            mAuditTheFail.setData(type, data);
            mTitleBar.setTitle("审核结果");
            mWaitingForAudit.getView().setVisibility(View.GONE);
            mAuditTheFail.getView().setVisibility(View.VISIBLE);
            mAuditTheResult.getView().setVisibility(View.GONE);
        } else {
            mAuditTheResult.setData(type, data);
            mTitleBar.setTitle("审核结果");
            mWaitingForAudit.getView().setVisibility(View.GONE);
            mAuditTheFail.getView().setVisibility(View.GONE);
            mAuditTheResult.getView().setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWaitingForAudit.onDestroy();
        mWaitingForAudit = null;
        mAuditTheResult.onDestroy();
        mAuditTheResult = null;
        mAuditTheFail.onDestroy();
        mAuditTheFail = null;
    }


}
