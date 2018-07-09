package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.OrderDetailsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.Convert;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.OrderOperation;
import com.shichuang.sendnar.common.OrderStatus;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ExchangeGiftOrderDetails;
import com.shichuang.sendnar.entify.LogisticsInformation;
import com.shichuang.sendnar.entify.OrderDetails;
import com.shichuang.sendnar.event.UpdateOrderEvent;
import com.shichuang.sendnar.widget.ConfirmDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ExchangeGiftOrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mRlLogisticsStatus;
    private TextView mTvLogisticsInfo;
    private TextView mTvLogisticsTime;
    // 收货人
    private RelativeLayout mRlReceiveAddress;
    private TextView mTvConsignee;
    private TextView mTvDeliveryAddress;
    // 订单编号，状态
    private TextView mTvOrderNo;
    private TextView mTvOrderStatus;
    // 商品列表
//    private RecyclerView mRecyclerView;
//    private OrderDetailsAdapter mAdapter;
    // 原商品
    private ImageView mIvPicture;
    private TextView mTvGiftsName;
    private TextView mTvGiftsPrice;
    // 新商品
    private ImageView mIvPictureNew;
    private TextView mTvGiftsNameNew;
    private TextView mTvGiftsPriceNew;
    // 发票信息
    private TextView mTvInvoiceType;
    private TextView mTvInvoiceContent;
    // 商品合计
    private TextView mTvGoodsTogetherAmount;
    private TextView mTvFreightAmount;
    private TextView mTvConsumptionPoints;
    // 积分抵扣
    private TextView mTvPointsDeductionAmount;
    // 实付金额
    private TextView mTvActuallyPaidAmount;
    // 共几件商品
    private TextView mTvGoodsCount;
    private TextView mTvOrderNo01;
    private TextView mTvCreateTimeOrder;
    private TextView mTvPaymentTime;
    private TextView mTvSendOutTime;
    private TextView mTvCloseTime;
    // 订单状态按钮
    private Button mBtnCancelOrder;
    private Button mBtnCheckLogistics;
    private Button mBtnDeleteOrder;
    private Button mBtnConfirmGoods;
    private Button mBtnImmediatePayment;
    // 空布局
    private RxEmptyLayout mEmptyLayout;

    private int id;
    private String orderNo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exchange_gift_order_details;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        id = getIntent().getIntExtra("id", 0);
        mRlLogisticsStatus = (RelativeLayout) findViewById(R.id.rl_logistics_status);
        mTvLogisticsInfo = (TextView) findViewById(R.id.tv_logistics_info);
        mTvLogisticsTime = (TextView) findViewById(R.id.tv_logistics_time);
        mRlReceiveAddress = (RelativeLayout) findViewById(R.id.rl_receive_address);
        mTvConsignee = (TextView) findViewById(R.id.tv_consignee);
        mTvDeliveryAddress = (TextView) findViewById(R.id.tv_delivery_address);
        mTvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        mTvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        initRecyclerView();

        mIvPicture = (ImageView) findViewById(R.id.iv_picture);
        mTvGiftsName = (TextView) findViewById(R.id.tv_gifts_name);
        mTvGiftsPrice = (TextView) findViewById(R.id.tv_gifts_price);
        mIvPictureNew = (ImageView) findViewById(R.id.iv_picture_new);
        mTvGiftsNameNew = (TextView) findViewById(R.id.tv_gifts_name_new);
        mTvGiftsPriceNew = (TextView) findViewById(R.id.tv_gifts_price_new);

        mTvInvoiceType = (TextView) findViewById(R.id.tv_invoice_type);
        mTvInvoiceContent = (TextView) findViewById(R.id.tv_invoice_content);
        mTvGoodsTogetherAmount = (TextView) findViewById(R.id.tv_goods_together_amount);
        mTvFreightAmount = (TextView) findViewById(R.id.tv_freight_amount);
        mTvConsumptionPoints = (TextView) findViewById(R.id.tv_consumption_points);
        mTvPointsDeductionAmount = (TextView) findViewById(R.id.tv_points_deduction_amount);
        mTvActuallyPaidAmount = (TextView) findViewById(R.id.tv_actually_paid_amount);
        mTvGoodsCount = (TextView) findViewById(R.id.tv_goods_count);
        mTvOrderNo01 = (TextView) findViewById(R.id.tv_order_no_01);
        mTvCreateTimeOrder = (TextView) findViewById(R.id.tv_create_time_order);
        mTvPaymentTime = (TextView) findViewById(R.id.tv_payment_time);
        mTvSendOutTime = (TextView) findViewById(R.id.tv_send_out_time);
        mTvCloseTime = (TextView) findViewById(R.id.tv_close_time);
        mBtnCancelOrder = (Button) findViewById(R.id.btn_cancel_order);
        mBtnCheckLogistics = (Button) findViewById(R.id.btn_check_logistics);
        mBtnDeleteOrder = (Button) findViewById(R.id.btn_delete_order);
        mBtnConfirmGoods = (Button) findViewById(R.id.btn_confirm_goods);
        mBtnImmediatePayment = (Button) findViewById(R.id.btn_immediate_payment);
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);

        EventBus.getDefault().register(this);
    }

    private void initRecyclerView() {
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
//        mLayoutManager.setScrollEnabled(false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new OrderDetailsAdapter();
//        mAdapter.setPreLoadNumber(2);
//        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mRlLogisticsStatus.setOnClickListener(this);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                getOrderDetailsData();
            }
        });
        mBtnCancelOrder.setOnClickListener(this);
        mBtnCheckLogistics.setOnClickListener(this);
        mBtnDeleteOrder.setOnClickListener(this);
        mBtnConfirmGoods.setOnClickListener(this);
        mBtnImmediatePayment.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getOrderDetailsData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_logistics_status:
                Bundle bundle2 = new Bundle();
                bundle2.putString("logisticsNo", orderNo);
                bundle2.putString("logisticsCompany", orderNo);
                RxActivityTool.skipActivity(mContext, LogisticsStatusActivity.class, bundle2);
                break;
            case R.id.btn_cancel_order:
                ConfirmDialog mDialogCancelOrder = new ConfirmDialog(mContext);
                mDialogCancelOrder.setMessage("确认取消订单？");
                mDialogCancelOrder.setNegativeButton("取消", null);
                mDialogCancelOrder.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        OrderOperation.getInstance().cancelOrder(mContext, id);
                    }
                });
                mDialogCancelOrder.show();
                break;
            case R.id.btn_check_logistics:
                Bundle bundle = new Bundle();
                bundle.putString("orderId", id + "");
                RxActivityTool.skipActivity(mContext, LogisticsStatusActivity.class, bundle);
                break;
            case R.id.btn_delete_order:
                ConfirmDialog mDialogDeleteOrder = new ConfirmDialog(mContext);
                mDialogDeleteOrder.setMessage("确认删除订单？");
                mDialogDeleteOrder.setNegativeButton("取消", null);
                mDialogDeleteOrder.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        OrderOperation.getInstance().deleteOrder(mContext, id);
                    }
                });
                mDialogDeleteOrder.show();
                break;
            case R.id.btn_confirm_goods:
                ConfirmDialog mDialogConfirmGoods = new ConfirmDialog(mContext);
                mDialogConfirmGoods.setMessage("确认已收到货物？");
                mDialogConfirmGoods.setNegativeButton("取消", null);
                mDialogConfirmGoods.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        OrderOperation.getInstance().confirmGoodsOrder(mContext, id);
                    }
                });
                mDialogConfirmGoods.show();
                break;
            case R.id.btn_immediate_payment:
                Bundle bundle1 = new Bundle();
                bundle1.putString("orderNo", orderNo);
                RxActivityTool.skipActivity(mContext, OrderSettlementActivity.class, bundle1);
                break;
            default:
                break;
        }
    }

    private void getOrderDetailsData() {
        OkGo.<AMBaseDto<ExchangeGiftOrderDetails>>get(Constants.orderDetailsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", id)
                .execute(new NewsCallback<AMBaseDto<ExchangeGiftOrderDetails>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ExchangeGiftOrderDetails>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ExchangeGiftOrderDetails>> response) {
                        AMBaseDto amBaseDto = response.body();
                        if (amBaseDto.code == 0 && amBaseDto.data != null) {
                            handleData((ExchangeGiftOrderDetails) amBaseDto.data);
                            mEmptyLayout.hide();
                        } else {
                            showToast(amBaseDto.msg);
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ExchangeGiftOrderDetails>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData(ExchangeGiftOrderDetails data) {
        // 判断是不是送礼商品
//        if (data.getSendGiftType() == 1) { // 送礼未接收
//            mRlReceiveAddress.setVisibility(View.GONE);
//        } else if (data.getSendGiftType() == 2) { // 送礼已接收
//            mRlReceiveAddress.setVisibility(View.VISIBLE);
//        } else {
//            mRlReceiveAddress.setVisibility(View.VISIBLE);
//        }
        ExchangeGiftOrderDetails.ExchangeOrder orderInfo = data.getExchangeOrder();
        orderNo = orderInfo.getOrderNo();
        // 收货地址
//        mTvConsignee.setText(orderInfo.getReceiveMan() + "   " + orderInfo.getContactPhone());
//        mTvDeliveryAddress.setText(orderInfo.getReceiveAddress());
        // 订单编号，状态
        mTvOrderNo.setText("订单编号：" + orderInfo.getOrderNo());
        // 发票信息
//        if (TextUtils.isEmpty(data.getInvoiceType()) || TextUtils.isEmpty(data.getInvoiceHead()) || TextUtils.isEmpty(data.getInvoiceContent())) {
//            mTvInvoiceType.setText("未选择发票");
//            mTvInvoiceContent.setVisibility(View.GONE);
//        } else {
//            mTvInvoiceType.setText(data.getInvoiceType() + "-" + data.getInvoiceHead());
//            mTvInvoiceContent.setText(data.getInvoiceContent());
//        }
        mTvInvoiceType.setText("未选择发票");
        mTvInvoiceContent.setVisibility(View.GONE);
        // 订单金额，付款信息
        mTvGoodsTogetherAmount.setText("¥" + (!TextUtils.isEmpty(orderInfo.getPriceDifference()) ? RxBigDecimalTool.toDecimal(orderInfo.getPriceDifference(), 2) : "0.00"));
        mTvActuallyPaidAmount.setText("¥" + (!TextUtils.isEmpty(orderInfo.getActualAmount()) ? RxBigDecimalTool.toDecimal(orderInfo.getActualAmount(), 2) : "0.00"));
        mTvPointsDeductionAmount.setText("¥" + (!TextUtils.isEmpty(orderInfo.getConsumptionPoints()) ? RxBigDecimalTool.toDecimal(orderInfo.getConsumptionPoints(), 2) : "0.00"));
        // mTvGoodsCount.setText("共" + data.getOrderDetailsGoodsList().size() + "件商品，实付：");
        mTvConsumptionPoints.setText("¥" + (!TextUtils.isEmpty(orderInfo.getOriginalGoodsPurpose()) ? RxBigDecimalTool.toDecimal(orderInfo.getOriginalGoodsPurpose(), 2) : "0.00"));
        mTvOrderNo01.setText("订单编号：" + orderInfo.getOrderNo());
        mTvCreateTimeOrder.setText("创建时间：" + (!TextUtils.isEmpty(orderInfo.getCreateTime()) ? RxTimeTool.stringFormat(orderInfo.getCreateTime()) : ""));
//        if (data.getSendGiftType() == 2) {   // 送礼已接收
//            mTvPaymentTime.setText("接收时间：" + (!TextUtils.isEmpty(orderInfo.getCreateTime()) ? RxTimeTool.stringFormat(orderInfo.getCreateTime()) : ""));
//        } else {
//            mTvPaymentTime.setText("付款时间：" + (!TextUtils.isEmpty(orderInfo.getPayTime()) ? RxTimeTool.stringFormat(orderInfo.getPayTime()) : ""));
//        }
        mTvPaymentTime.setText("付款时间：" + (!TextUtils.isEmpty(orderInfo.getPayTime()) ? RxTimeTool.stringFormat(orderInfo.getPayTime()) : ""));
        //mTvSendOutTime.setText("送货时间：" + (!TextUtils.isEmpty(orderInfo.getSendOutTime()) ? RxTimeTool.stringFormat(orderInfo.getSendOutTime()) : ""));
        mTvCloseTime.setText("关闭时间：" + (!TextUtils.isEmpty(orderInfo.getClosingTime()) ? RxTimeTool.stringFormat(orderInfo.getClosingTime()) : ""));
        // 商品列表
//        mAdapter.replaceData(data.getOrderDetailsGoodsList());
//        mAdapter.setOrderStatus(orderInfo.getOrderStatus());
        // 原商品信息
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + orderInfo.getNewGoodsPic(), mIvPicture);
        mTvGiftsName.setText(orderInfo.getNewGoodsName());
        mTvGiftsPrice.setText("¥" + RxBigDecimalTool.toDecimal(orderInfo.getNewGoodsPrice(), 2));
        // 换货商品信息
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + orderInfo.getOriginalGoodsPic(), mIvPictureNew);
        mTvGiftsNameNew.setText(orderInfo.getOriginalGoodsName());
        mTvGiftsPriceNew.setText("¥" + RxBigDecimalTool.toDecimal(orderInfo.getOriginalGoodsPrice(), 2));
        // 设置商品操作状态
        setOrderOperation(orderInfo.getOrderStatus());
        // 获取物流消息
        //getLogisticsData();
    }

    private void setOrderOperation(int status) {

        switch (status) {
            // "待付款"
            case OrderStatus.WAIT_PAYMENT:
                mTvOrderStatus.setText("待付款");
                mTvPaymentTime.setVisibility(View.GONE);
                mTvSendOutTime.setVisibility(View.GONE);
                mTvCloseTime.setVisibility(View.GONE);

                //mRlLogisticsStatus.setVisibility(View.GONE);
                mBtnCancelOrder.setVisibility(View.VISIBLE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.VISIBLE);
                break;
            // "待发货"
            case OrderStatus.WAIT_WAIT_DELIVERY:
                mTvOrderStatus.setText("待发货");
                mTvPaymentTime.setVisibility(View.VISIBLE);
                mTvSendOutTime.setVisibility(View.GONE);
                mTvCloseTime.setVisibility(View.GONE);

                //mRlLogisticsStatus.setVisibility(View.GONE);
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            // "待收货"
            case OrderStatus.DELIVERED:
                mTvOrderStatus.setText("待收货");
                mTvPaymentTime.setVisibility(View.VISIBLE);
                mTvSendOutTime.setVisibility(View.VISIBLE);
                mTvCloseTime.setVisibility(View.GONE);

                //mRlLogisticsStatus.setVisibility(View.VISIBLE);
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.VISIBLE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.VISIBLE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            // "已完成"
            case OrderStatus.COMPLETED:
                mTvOrderStatus.setText("已完成");
                mTvPaymentTime.setVisibility(View.VISIBLE);
                mTvSendOutTime.setVisibility(View.VISIBLE);
                mTvCloseTime.setVisibility(View.GONE);

                //mRlLogisticsStatus.setVisibility(View.VISIBLE);
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.VISIBLE);
                mBtnDeleteOrder.setVisibility(View.VISIBLE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            // "交易关闭"
            case OrderStatus.TRADING_CLOSED:
                mTvOrderStatus.setText("交易关闭");
                mTvPaymentTime.setVisibility(View.GONE);
                mTvSendOutTime.setVisibility(View.GONE);
                mTvCloseTime.setVisibility(View.VISIBLE);

                //mRlLogisticsStatus.setVisibility(View.GONE);
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.VISIBLE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            default:
                mTvOrderStatus.setText("");
                mTvPaymentTime.setVisibility(View.GONE);
                mTvSendOutTime.setVisibility(View.GONE);
                mTvCloseTime.setVisibility(View.GONE);

                //mRlLogisticsStatus.setVisibility(View.GONE);
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
        }
    }

    private void getLogisticsData() {
        OkGo.<AMBaseDto<String>>get(Constants.getLogisticsMsgUrl)
                .tag(mContext)
                .params("express_no", "")
                .params("com", "")
                .execute(new NewsCallback<AMBaseDto<String>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<String>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<String>> response) {
                        if (response.body().code == 0) {
                            String info = response.body().data;
                            if (!TextUtils.isEmpty(info)) {
                                LogisticsInformation logisticsInformation = Convert.fromJson(info, LogisticsInformation.class);
                                if (logisticsInformation != null) {
                                    // 获取数据第一条信息
                                    if (logisticsInformation.getData().size() > 0) {
                                        LogisticsInformation.LogisticsInformationModel logistics = logisticsInformation.getData().get(0);
                                        mTvLogisticsInfo.setText(logistics.getContext());
                                        mTvLogisticsTime.setText(logistics.getTime());
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<String>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateOrderEvent event) {/* Do something */
        if (event != null) {
            if (event.operationType == UpdateOrderEvent.DELETE_ORDER) {
                RxActivityTool.finish(mContext);
            } else {
                getOrderDetailsData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
