package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.ConfirmOrderAdapter;
import com.shichuang.sendnar.adapter.GiftReceivedConfirmOrderAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.entify.CommitOrder;
import com.shichuang.sendnar.entify.ConfirmOrder;

import java.util.List;

/**
 * 礼物接收，提交订单
 * Created by Administrator on 2018/5/8.
 */

public class GiftReceivedConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    private static final int SELECT_ADDRESS = 0x11;
    private RxEmptyLayout mEmptyLayout;
    private LinearLayout mLlAddAddress;
    private RelativeLayout mRlAddress;
    private TextView mTvConsignee;
    private TextView mTvPhone;
    private TextView mTvDetailsAddress;
    private RecyclerView mRecyclerView;
    private GiftReceivedConfirmOrderAdapter mAdapter;
    // 商品合计
    private TextView mTvGoodsTogetherAmount;

    // 商品Id
    private int goodsId;
    // 礼物订单ID
    private int giftOrderId;
    // 地址Id
    private int addressId;
    // 购买类型（微信送礼，送给自己）

    @Override
    public int getLayoutId() {
        return R.layout.activity_gift_received_confirm_order;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        goodsId = getIntent().getIntExtra("goodsId", 0);
        giftOrderId = getIntent().getIntExtra("giftOrderId", 0);
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mLlAddAddress = (LinearLayout) findViewById(R.id.ll_add_address);
        mRlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        mTvConsignee = (TextView) findViewById(R.id.tv_consignee);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvDetailsAddress = (TextView) findViewById(R.id.tv_details_address);
        mTvGoodsTogetherAmount = (TextView) findViewById(R.id.tv_goods_together_amount);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GiftReceivedConfirmOrderAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mRlAddress.setOnClickListener(this);
        mLlAddAddress.setOnClickListener(this);
        findViewById(R.id.btn_complete).setOnClickListener(this);
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
            case R.id.rl_address:
            case R.id.ll_add_address:
                Bundle bundle = new Bundle();
                bundle.putInt("from", MyAddressActivity.CONFIRM_ORDER);
                RxActivityTool.skipActivityForResult(GiftReceivedConfirmOrderActivity.this, MyAddressActivity.class, bundle, SELECT_ADDRESS);
                break;
            // 结算
            case R.id.btn_complete:
                checkInfo();
                break;
            default:
                break;
        }
    }

    /**
     * 接收礼物 数据
     */
    private void getOrderData() {
        OkGo.<AMBaseDto<ConfirmOrder>>get(Constants.confirmOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_id", goodsId)
                .params("address_id", addressId == 0 ? "" : String.valueOf(addressId))
                .execute(new NewsCallback<AMBaseDto<ConfirmOrder>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ConfirmOrder>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ConfirmOrder>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                            mEmptyLayout.hide();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ConfirmOrder>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData(ConfirmOrder data) {
        // 处理收货地址
        List<ConfirmOrder.Address> addressList = data.getAddressList();
        if (addressList != null && addressList.size() > 0) {
            ConfirmOrder.Address address = addressList.get(0);
            addressId = address.getId();
            mTvConsignee.setText(address.getName());
            mTvPhone.setText(address.getPhone());
            mTvDetailsAddress.setText(address.getProvince() + address.getCity() + address.getArea() + address.getAddress());
            mRlAddress.setVisibility(View.VISIBLE);
            mLlAddAddress.setVisibility(View.GONE);
        } else {
            mRlAddress.setVisibility(View.GONE);
            mLlAddAddress.setVisibility(View.VISIBLE);
        }
        // 普通方式返回的商品数据为Model，购物车为List
        if (data.getGoods() != null) {
            mAdapter.addData(data.getGoods());
        }
        mTvGoodsTogetherAmount.setText("¥" + RxBigDecimalTool.toDecimal(data.getTotalPrice(), 2));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_ADDRESS && resultCode == RESULT_OK) {
            Address address = (Address) data.getSerializableExtra("address");
            addressId = address.getId();
            mTvConsignee.setText(address.getName());
            mTvPhone.setText(address.getPhone());
            mTvDetailsAddress.setText(address.getProvinceName() + address.getCityName() + address.getAreaName() + address.getAddress());
            mRlAddress.setVisibility(View.VISIBLE);
            mLlAddAddress.setVisibility(View.GONE);
        }
    }


    private void checkInfo() {
        if (addressId == 0) {
            showToast("请选择收货地址");
        } else {
            orderSettle();
        }
    }

    /**
     * 订单结算
     */
    private void orderSettle() {
        OkGo.<AMBaseDto<CommitOrder>>post(Constants.receiveGiftSubmitOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("user_address_id", addressId)
                .params("goods_id", goodsId)
                .params("id", giftOrderId)    // 礼物订单id
                .execute(new NewsCallback<AMBaseDto<CommitOrder>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<CommitOrder>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<CommitOrder>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0 && response.body().data != null) {
                            RxActivityTool.finish(mContext, RESULT_OK);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<CommitOrder>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
