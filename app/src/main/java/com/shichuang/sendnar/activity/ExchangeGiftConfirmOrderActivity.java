package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.ConfirmOrderAdapter;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.entify.CommitOrder;
import com.shichuang.sendnar.entify.ConfirmOrder;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.entify.ExchangeGiftCommitOrder;
import com.shichuang.sendnar.entify.ExchangeGiftConfirmOrder;
import com.shichuang.sendnar.entify.Invoice;
import com.shichuang.sendnar.event.UpdateShoppingCart;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 换礼物-确认订单
 * Created by Administrator on 2018/4/18.
 */

public class ExchangeGiftConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    private static final int SELECT_INVOICE = 0x12;
    // 原商品
    private ImageView mIvPicture;
    private TextView mTvGiftsName;
    private TextView mTvGiftsPrice;
    // 新商品
    private ImageView mIvPictureNew;
    private TextView mTvGiftsNameNew;
    private TextView mTvGiftsPriceNew;

    private RxEmptyLayout mEmptyLayout;
    private ImageView mIvIntegralSwitch;
    private TextView mTvInvoiceType;
    private TextView mTvTotalCount;
    private TextView mTvActuallyPaid;
    // 积分抵扣
    private TextView mTvPointsDeduction;
    // 商品合计
    private TextView mTvGoodsTogetherAmount;
    // 原商品抵用积分
    private TextView mTvConsumptionPoints;

    private ExchangeGift exchangeGift;
    private int buyType;
    // 发票信息
    private Invoice mInvoice;
    // 订单信息
    private ExchangeGiftConfirmOrder orderData;
    // 积分
    private double point;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exchange_gift_confirm_order;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        exchangeGift = (ExchangeGift) getIntent().getSerializableExtra("exchangeGift");
        buyType = getIntent().getIntExtra("buyType", 0);

        mIvPicture = (ImageView) findViewById(R.id.iv_picture);
        mTvGiftsName = (TextView) findViewById(R.id.tv_gifts_name);
        mTvGiftsPrice = (TextView) findViewById(R.id.tv_gifts_price);
        mIvPictureNew = (ImageView) findViewById(R.id.iv_picture_new);
        mTvGiftsNameNew = (TextView) findViewById(R.id.tv_gifts_name_new);
        mTvGiftsPriceNew = (TextView) findViewById(R.id.tv_gifts_price_new);

        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mIvIntegralSwitch = (ImageView) findViewById(R.id.iv_integral_switch);
        mTvInvoiceType = (TextView) findViewById(R.id.tv_invoice_type);
        mTvTotalCount = (TextView) findViewById(R.id.tv_total_count);
        mTvPointsDeduction = (TextView) findViewById(R.id.tv_points_deduction);
        mTvGoodsTogetherAmount = (TextView) findViewById(R.id.tv_goods_together_amount);
        mTvConsumptionPoints = (TextView) findViewById(R.id.tv_consumption_points);
        mTvActuallyPaid = (TextView) findViewById(R.id.tv_actually_paid);
    }


    @Override
    public void initEvent() {
        findViewById(R.id.ll_select_invoice).setOnClickListener(this);
        mIvIntegralSwitch.setOnClickListener(this);
        findViewById(R.id.btn_to_settle_accounts).setOnClickListener(this);

        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                getOrderData();
            }
        });
    }

    @Override
    public void initData() {
        getOrderData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_integral_switch:
                isUseIntegral();
                break;
            case R.id.ll_select_invoice:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("invoice", mInvoice);
                RxActivityTool.skipActivityForResult(ExchangeGiftConfirmOrderActivity.this, FillInTheInvoiceInformationActivity.class, bundle1, SELECT_INVOICE);
                break;
            // 结算
            case R.id.btn_to_settle_accounts:
                checkInfo();
                break;
            default:
                break;
        }
    }

    private void isUseIntegral() {
        // 判断是否可使用积分,实付金额大于0时，可用积分
        double newGoodPrice = Double.valueOf(orderData.getNewGoods().getSalePrice());
        double originalGoodPoint = Double.valueOf(orderData.getPurpose());

        if ((newGoodPrice - originalGoodPoint) > 0 && point > 0) {
            if (mIvIntegralSwitch.isSelected()) {
                mIvIntegralSwitch.setSelected(false);
            } else {
                mIvIntegralSwitch.setSelected(true);
            }
            handlePrice();
        }else{
            showToast("新商品价格小于原商品，不能使用积分");
        }
    }

    /**
     * 换礼物 数据
     */
    private void getOrderData() {
        OkGo.<AMBaseDto<ExchangeGiftConfirmOrder>>post(Constants.exchangeGiftConfirmOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", exchangeGift.getId())
                .params("receive_gift_id", exchangeGift.getReceiveGiftId())
                .params("goods_b_id", exchangeGift.getGoodIdNew())
                .execute(new NewsCallback<AMBaseDto<ExchangeGiftConfirmOrder>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ExchangeGiftConfirmOrder>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ExchangeGiftConfirmOrder>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                            mEmptyLayout.hide();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ExchangeGiftConfirmOrder>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    private void handleData(ExchangeGiftConfirmOrder data) {
        this.orderData = data;
        // 原商品信息
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + data.getNewGoods().getPic(), mIvPicture);
        mTvGiftsName.setText(data.getNewGoods().getShopName());
        mTvGiftsPrice.setText("¥" + RxBigDecimalTool.toDecimal(data.getNewGoods().getSalePrice(), 2));
        // 换货商品信息
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + data.getOriginalGood().getPic(), mIvPictureNew);
        mTvGiftsNameNew.setText(data.getOriginalGood().getShopName());
        mTvGiftsPriceNew.setText("¥" + RxBigDecimalTool.toDecimal(data.getOriginalGood().getSalePrice(), 2));
        // 积分
        point = Double.valueOf(data.getPoint());
        if (point > 0) {
            mTvPointsDeduction.setText("未使用积分");
        } else {
            mTvPointsDeduction.setText("可用积分为0");
        }
        mTvConsumptionPoints.setText("¥" + RxBigDecimalTool.toDecimal(data.getPurpose(), 2));
        mTvGoodsTogetherAmount.setText("¥" + RxBigDecimalTool.toDecimal(data.getNewGoods().getSalePrice(), 2));
        // 实付金额计算
        handlePrice();

        //mTvActuallyPaid.setText("¥ " + RxBigDecimalTool.toDecimal(data.getActualAmount(), 2));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_INVOICE && resultCode == RESULT_OK) {
            mInvoice = (Invoice) data.getSerializableExtra("invoice");
            mTvInvoiceType.setText((mInvoice.getType() == 1 ? "普通发票" : "电子发票") + "  " + (mInvoice.getHead() == 1 ? "个人" : "单位"));
        }
    }

    private void checkInfo() {
        orderSettle();
    }

    /**
     * （送给自己）订单结算
     */
    private void orderSettle() {
        OkGo.<AMBaseDto<ExchangeGiftCommitOrder>>get(Constants.exchangeGiftSubmitOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", exchangeGift.getId())   // 送礼订单id
                .params("receive_gift_id", exchangeGift.getReceiveGiftId())  // 礼物袋id
                .params("goods_b_id", exchangeGift.getGoodIdNew())   // 新商品id
                .params("point_obversion", mIvIntegralSwitch.isSelected() ? 1 : 2)   // 积分抵扣 1 抵扣 || 2 不抵扣
                .params("type", mInvoice == null ? -1 : mInvoice.getType())   // 发票信息
                .params("head", mInvoice == null ? -1 : mInvoice.getHead())   // 发票信息
                .params("company_name", mInvoice == null ? "" : mInvoice.getCompanyName())   // 发票信息
                .params("user_code", mInvoice == null ? "" : mInvoice.getUserCode())   // 发票信息
                .params("goods_type", mInvoice == null ? -1 : mInvoice.getGoodsType())   // 发票信息
                .params("email", mInvoice == null ? "" : mInvoice.getEmail())   // 发票信息
                .params("phone_num", mInvoice == null ? "" : mInvoice.getPhoneNum())   // 发票信息
                .execute(new NewsCallback<AMBaseDto<ExchangeGiftCommitOrder>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ExchangeGiftCommitOrder>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ExchangeGiftCommitOrder>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0 && response.body().data != null) {
                            ExchangeGiftCommitOrder commitOrder = response.body().data;
                            if (commitOrder.getIsPay() == 1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderNo", commitOrder.getOrderNo());
                                bundle.putInt("buyType", buyType);
                                RxActivityTool.skipActivity(mContext, OrderSettlementActivity.class, bundle);
                                ExchangeGiftConfirmOrderActivity.this.finish();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("payResult", true);
                                bundle.putInt("buyType", buyType);
                                bundle.putString("orderNo", commitOrder.getOrderNo());
                                RxActivityTool.skipActivity(mContext, PayResultActivity.class, bundle);
                                ExchangeGiftConfirmOrderActivity.this.finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ExchangeGiftCommitOrder>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
    }


    private void handlePrice() {
//        // 商品合计金额
//        double totalAmount = 0.00;
//        // 积分金额
//        double pointAmount = 0.00;
//        // 实付金额
//        double realPayAmount = 0.00;
//
//
////        for (ConfirmOrder.Goods model : mList) {
////            totalAmount = RxBigDecimalTool.add(totalAmount, Double.valueOf(model.getSalePrice()) * model.getCount());
////        }
//        mTvGoodsTogetherAmount.setText("¥" + RxBigDecimalTool.toDecimal(totalAmount, 2));
//        // 是否可以抵扣积分
//        if (mIvIntegralSwitch.isSelected() && point > 0) {
//            pointAmount = point;
//            // 金额大于积分
//            if (totalAmount > pointAmount) {
//                mTvPointsDeduction.setText("¥" + RxBigDecimalTool.toDecimal(pointAmount, 2));
//                realPayAmount = RxBigDecimalTool.sub(totalAmount, pointAmount);
//            } else { // 积分大于金额
//                mTvPointsDeduction.setText("¥" + RxBigDecimalTool.toDecimal(totalAmount, 2));
//                realPayAmount = 0.00;
//            }
//        } else {
//            realPayAmount = totalAmount;
//            if (point > 0) {
//                mTvPointsDeduction.setText("未使用积分");
//            }
//        }
//        // 实际支付金额
//        mTvActuallyPaid.setText("¥ " + RxBigDecimalTool.toDecimal(realPayAmount, 2));


        double newGoodPrice = Double.valueOf(orderData.getNewGoods().getSalePrice());
        double originalGoodPoint = Double.valueOf(orderData.getPurpose());

        // 使用积分
        if (mIvIntegralSwitch.isSelected() && point > 0) {
            if (point > (newGoodPrice - originalGoodPoint)) {
                mTvPointsDeduction.setText("¥" + RxBigDecimalTool.toDecimal(newGoodPrice - originalGoodPoint, 2));
                mTvActuallyPaid.setText("¥ 0.00");
            } else {
                double differencePrice = newGoodPrice - originalGoodPoint;
                mTvPointsDeduction.setText("¥" + RxBigDecimalTool.toDecimal(differencePrice - point, 2));
                mTvActuallyPaid.setText("¥ " + RxBigDecimalTool.toDecimal(differencePrice - point, 2));
            }
        } else {  // 未使用积分
            mTvPointsDeduction.setText("未使用积分");

            if (newGoodPrice > originalGoodPoint) {
                mTvActuallyPaid.setText("¥ " + RxBigDecimalTool.toDecimal(newGoodPrice - originalGoodPoint, 2));
            } else {
                mTvActuallyPaid.setText("¥ 0.00");
            }
        }
    }
}
