package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.OrderSettleAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.entify.OrderSettle;
import com.shichuang.sendnar.entify.WxMakeOrder;
import com.shichuang.sendnar.widget.ConfirmDialog;
import com.shichuang.sendnar.widget.payment.OnRequestListener;
import com.shichuang.sendnar.widget.payment.alipay.AliPayModel;
import com.shichuang.sendnar.widget.payment.alipay.AliPayTools;
import com.shichuang.sendnar.widget.payment.wechat.pay.WechatPayTools;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Administrator on 2018/4/19.
 */

public class OrderSettlementActivity extends BaseActivity implements View.OnClickListener {
    private RxEmptyLayout mEmptyLayout;
    private RecyclerView mRecyclerView;
    private OrderSettleAdapter mAdapter;
    private TextView mTvActuallyPaid;
    private ImageView mIvWechatPay;
    private ImageView mIvAliPay;

    private String orderNo;
    private OrderSettle orderSettle;
    // 购买类型
    private int buyType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_settlement;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderNo = getIntent().getStringExtra("orderNo");
        buyType = getIntent().getIntExtra("buyType", 0);
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        initRecyclerView();
        mTvActuallyPaid = (TextView) findViewById(R.id.tv_actually_paid);
        mIvWechatPay = (ImageView) findViewById(R.id.iv_wechat_pay);
        mIvAliPay = (ImageView) findViewById(R.id.iv_ali_pay);

        // 默认支付宝支付
        selectPayMethod(1);
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrderSettleAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(this);
        findViewById(R.id.ll_wechat_pay).setOnClickListener(this);
        findViewById(R.id.ll_ali_pay).setOnClickListener(this);
        findViewById(R.id.btn_payment).setOnClickListener(this);

        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                getData();
            }
        });
    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                showRemind();
                break;
            case R.id.ll_wechat_pay:
                selectPayMethod(0);
                break;
            case R.id.ll_ali_pay:
                selectPayMethod(1);
                break;
            case R.id.btn_payment:
                pay();
                break;
            default:
                break;
        }
    }

    private void getData() {
        OkGo.<AMBaseDto<OrderSettle>>get(Constants.getOrderInfoByOrderNoUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("order_no", orderNo)
                .execute(new NewsCallback<AMBaseDto<OrderSettle>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<OrderSettle>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<OrderSettle>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0 && response.body().data != null) {
                            orderSettle = response.body().data;
                            handleData();
                            mEmptyLayout.hide();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<OrderSettle>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    private void handleData() {
        if (orderSettle.getGoodsList() != null) {
            mAdapter.addData(orderSettle.getGoodsList());
        }
        mTvActuallyPaid.setText("应付款 ¥" + RxBigDecimalTool.toDecimal(orderSettle.getActualAmount(), 2));
    }

    /**
     * 0 wechat pay,1 ali pay
     *
     * @param i
     */
    private void selectPayMethod(int i) {
        if (i == 0) {
            mIvAliPay.setSelected(false);
            mIvWechatPay.setSelected(true);
        } else {
            mIvAliPay.setSelected(true);
            mIvWechatPay.setSelected(false);
        }
    }

    private void pay() {
        if (mIvWechatPay.isSelected()) {
            wechatPay();
        } else {
            aliPay();
        }
    }

    private void wechatPay() {
        String price = orderSettle.getActualAmount();
        String title = "送哪儿礼品订单";
        Log.d("test", orderNo + "   " + price + "    " + title);
        OkGo.<AMBaseDto<WxMakeOrder>>post(Constants.wxMakeOrderUrl)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .tag(context)
                .params("out_trade_no", orderNo)
                .params("subject", title)
                .params("total_fee", price)
                .execute(new NewsCallback<AMBaseDto<WxMakeOrder>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<WxMakeOrder>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<WxMakeOrder>> response) {
                        if (response.body().code == 0) {
                            if (response.body().data != null) {
                                WxMakeOrder data = response.body().data;
                                // 掉起微信支付
                                WechatPayTools.wechatPayApp(mContext,
                                        data.getAppId(),
                                        data.getPartnerId(),
                                        data.getPrepayId(),
                                        data.getPackageValue(),
                                        data.getNonceStr(),
                                        data.getTimeStamp(),
                                        data.getSign(),
                                        onRequestListener);
                            }
                        } else {
                            RxToastTool.showShort(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<WxMakeOrder>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });

    }


    private void aliPay() {
        String price = orderSettle.getActualAmount();
        String title = "送哪儿礼品订单";

        // 旧版支付
        AliPayTools.aliPayV1(OrderSettlementActivity.this,
                Constants.ALIPAY_PID, Constants.ALIPAY_SELLER,
                Constants.ALIPAY_RSA_PRIVATE, Constants.ALIPAY_NOTIFY_URL,
                new AliPayModel(orderNo,//订单ID (唯一)
                        price,//价格
                        title,//商品名称
                        title),//商品描述详情 (用于显示在 支付宝 的交易记录里));
                onRequestListener);
    }


    private OnRequestListener onRequestListener = new OnRequestListener() {

        @Override
        public void onSuccess(String s) {
            skipPayResultPage(true);
        }

        @Override
        public void onError(String s) {
            skipPayResultPage(false);
        }
    };

    private void skipPayResultPage(boolean isPaySuccess) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("payResult", isPaySuccess);
        bundle.putInt("buyType", buyType);
        RxActivityTool.skipActivity(mContext, PayResultActivity.class, bundle);
        OrderSettlementActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showRemind();
    }

    private void showRemind() {
        ConfirmDialog mDialog = new ConfirmDialog(mContext);
        mDialog.hideTitle();
        mDialog.setMessage("宝贝错过就木有啦\n  真的不要了吗？");
        mDialog.setNegativeButton("去意已决", new ConfirmDialog.DialogInterface() {
            @Override
            public void OnClickListener() {
                RxActivityTool.finish(mContext);
            }
        });
        mDialog.setPositiveButton("我再想想", null);
        mDialog.show();
    }
}
