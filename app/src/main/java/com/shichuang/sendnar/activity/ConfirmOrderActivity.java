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
import com.shichuang.sendnar.entify.Invoice;
import com.shichuang.sendnar.event.UpdateShoppingCart;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 确认订单
 * Created by Administrator on 2018/4/18.
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    private static final int SELECT_ADDRESS = 0x11;
    private static final int SELECT_INVOICE = 0x12;
    private RxEmptyLayout mEmptyLayout;
    private LinearLayout mLlAddAddress;
    private RelativeLayout mRlAddress;
    private TextView mTvConsignee;
    private TextView mTvPhone;
    private TextView mTvDetailsAddress;
    private RecyclerView mRecyclerView;
    private ConfirmOrderAdapter mAdapter;
    private ImageView mIvIntegralSwitch;
    private TextView mTvInvoiceType;
    private TextView mTvTotalCount;
    private TextView mTvActuallyPaid;
    // 积分抵扣
    private TextView mTvPointsDeduction;
    // 商品合计
    private TextView mTvGoodsTogetherAmount;

    private int goodsId;
    // 默认商品数量(送给自己默认)
    private int count = 1;
    // 地址Id
    private int addressId;
    // 购买类型（微信送礼，送给自己）
    private int buyType;
    // 购物车Ids
    private String cardIds;
    // Song类型（1为微信送礼）
    private int songType = -1;
    // 发票信息
    private Invoice mInvoice;
    // 积分
    private double point;

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        goodsId = getIntent().getIntExtra("goodsId", 0);
        buyType = getIntent().getIntExtra("buyType", 0);
        count = getIntent().getIntExtra("count", 1);
        cardIds = getIntent().getStringExtra("cardIds");
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mLlAddAddress = (LinearLayout) findViewById(R.id.ll_add_address);
        mRlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        mTvConsignee = (TextView) findViewById(R.id.tv_consignee);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mTvDetailsAddress = (TextView) findViewById(R.id.tv_details_address);
        mIvIntegralSwitch = (ImageView) findViewById(R.id.iv_integral_switch);
        mTvInvoiceType = (TextView) findViewById(R.id.tv_invoice_type);
        mTvTotalCount = (TextView) findViewById(R.id.tv_total_count);
        mTvPointsDeduction = (TextView) findViewById(R.id.tv_points_deduction);
        mTvGoodsTogetherAmount = (TextView) findViewById(R.id.tv_goods_together_amount);
        mTvActuallyPaid = (TextView) findViewById(R.id.tv_actually_paid);
        initRecyclerView();
        // 如果是微信送礼，隐藏收货地址
        if (buyType == BuyType.WECHAT_GIFT_GIVING || buyType == BuyType.SHOPPING_CART_WECHAT_GIFT_GIVING) {
            findViewById(R.id.fl_address_layout).setVisibility(View.GONE);
            findViewById(R.id.iv_address_divide).setVisibility(View.GONE);
            songType = 1;
        } else {
            songType = -1;
        }
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ConfirmOrderAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mRlAddress.setOnClickListener(this);
        mLlAddAddress.setOnClickListener(this);
        findViewById(R.id.ll_select_invoice).setOnClickListener(this);
        mIvIntegralSwitch.setOnClickListener(this);
        findViewById(R.id.btn_to_settle_accounts).setOnClickListener(this);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 商品数量加减
                if (view.getId() == R.id.btn_subtract) {
                    subtractOrPlus(0, position);
                } else if (view.getId() == R.id.btn_plus) {
                    subtractOrPlus(1, position);
                }
            }
        });
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                getOrderData();
            }
        });
    }

    @Override
    public void initData() {
        if (buyType == BuyType.SHOPPING_CART_BUY_NOW || buyType == BuyType.SHOPPING_CART_WECHAT_GIFT_GIVING) {
            getShoppingCartOrderData();
        } else {
            getOrderData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address:
            case R.id.ll_add_address:
                Bundle bundle = new Bundle();
                bundle.putInt("from", MyAddressActivity.CONFIRM_ORDER);
                RxActivityTool.skipActivityForResult(ConfirmOrderActivity.this, MyAddressActivity.class, bundle, SELECT_ADDRESS);
                break;
            case R.id.iv_integral_switch:
                isUseIntegral();
                break;
            case R.id.ll_select_invoice:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("invoice", mInvoice);
                RxActivityTool.skipActivityForResult(ConfirmOrderActivity.this, FillInTheInvoiceInformationActivity.class, bundle1, SELECT_INVOICE);
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
        // 判断是否可使用积分
        if (point > 0) {
            if (mIvIntegralSwitch.isSelected()) {
                mIvIntegralSwitch.setSelected(false);
            } else {
                mIvIntegralSwitch.setSelected(true);
            }
            handlePrice();
        }
    }

    /**
     * 送给自己 数据
     */
    private void getOrderData() {
        OkGo.<AMBaseDto<ConfirmOrder>>get(Constants.confirmOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_count", count)
                .params("goods_id", goodsId)
                .params("song", songType)
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

    /**
     * 购物车 送给自己
     */
    private void getShoppingCartOrderData() {
        OkGo.<AMBaseDto<ConfirmOrder>>get(Constants.confirmShoppingCartOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("cart_ids", cardIds)   // 商品的购物车ids（逗号分隔）
                .params("address_id", addressId == 0 ? "" : String.valueOf(addressId))
                .params("song", songType)
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
        if (buyType == BuyType.SHOPPING_CART_BUY_NOW || buyType == BuyType.SHOPPING_CART_WECHAT_GIFT_GIVING) {
            if (data.getGoodsList() != null) {
                mAdapter.addData(data.getGoodsList());
                mTvTotalCount.setText("共" + data.getGoodsList().size() + "件商品，小计：");
            }
        } else {
            // 商品信息
            if (data.getGoods() != null) {
                data.getGoods().setCount(count);
                mAdapter.addData(data.getGoods());
            }
        }
        // 积分
        point = Double.valueOf(data.getPoint());
        if (point > 0) {
            mTvPointsDeduction.setText("未使用积分");
        } else {
            mTvPointsDeduction.setText("可用积分为0");
        }
        mTvGoodsTogetherAmount.setText("¥" + RxBigDecimalTool.toDecimal(data.getTotalPrice(), 2));
        mTvActuallyPaid.setText("¥ " + RxBigDecimalTool.toDecimal(data.getTotalPrice(), 2));
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
        } else if (requestCode == SELECT_INVOICE && resultCode == RESULT_OK) {
            mInvoice = (Invoice) data.getSerializableExtra("invoice");
            mTvInvoiceType.setText((mInvoice.getType() == 1 ? "普通发票" : "电子发票") + "  " + (mInvoice.getHead() == 1 ? "个人" : "单位"));
        }
    }


    private void checkInfo() {
        // 送给自己
        if (buyType == BuyType.BUY_NOW || buyType == BuyType.WECHAT_GIFT_GIVING) {
            if (buyType == BuyType.BUY_NOW) {
                if (addressId == 0) {
                    showToast("请选择收货地址");
                    return;
                }
            }
            orderSettle();
        }
        // 购物车结算
        else {
            if (buyType == BuyType.SHOPPING_CART_BUY_NOW) {
                if (addressId == 0) {
                    showToast("请选择收货地址");
                    return;
                }
            }
            shoppingCartOrderSettle();
        }

    }

    /**
     * （送给自己）订单结算
     */
    private void orderSettle() {
        OkGo.<AMBaseDto<CommitOrder>>get(Constants.submitOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("user_address_id", addressId)
                .params("point_obversion", mIvIntegralSwitch.isSelected() ? 1 : 2)   // 积分抵扣 1 抵扣 || 2 不抵扣
                .params("goods_id", goodsId)
                .params("count", mAdapter.getItem(0).getCount())
                .params("invoice_head", "测试")
                .params("invoice_content", "测试")
                .params("song", songType)   // 发送 1 为微信送礼
                .params("type", mInvoice == null ? -1 : mInvoice.getType())   // 发票信息
                .params("head", mInvoice == null ? -1 : mInvoice.getHead())   // 发票信息
                .params("company_name", mInvoice == null ? "" : mInvoice.getCompanyName())   // 发票信息
                .params("user_code", mInvoice == null ? "" : mInvoice.getUserCode())   // 发票信息
                .params("goods_type", mInvoice == null ? -1 : mInvoice.getGoodsType())   // 发票信息
                .params("email", mInvoice == null ? "" : mInvoice.getEmail())   // 发票信息
                .params("phone_num", mInvoice == null ? "" : mInvoice.getPhoneNum())   // 发票信息
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
                            CommitOrder commitOrder = response.body().data;
                            if (commitOrder.getIsPay() == 1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderNo", commitOrder.getOrderNo());
                                bundle.putInt("buyType",buyType);
                                RxActivityTool.skipActivity(mContext, OrderSettlementActivity.class, bundle);
                                ConfirmOrderActivity.this.finish();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("payResult", true);
                                bundle.putInt("buyType",buyType);
                                RxActivityTool.skipActivity(mContext, PayResultActivity.class, bundle);
                                ConfirmOrderActivity.this.finish();
                            }
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

    /**
     * （购物车）订单结算
     */
    private void shoppingCartOrderSettle() {
        OkGo.<AMBaseDto<CommitOrder>>get(Constants.submitShoppingCartOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("user_address_id", addressId)
                .params("point_obversion", mIvIntegralSwitch.isSelected() ? 1 : 2)   // 积分抵扣 1 抵扣 || 2 不抵扣
                .params("cart_ids", cardIds)
                .params("song", songType)   // 发送 1 为微信送礼
                .params("type", mInvoice == null ? -1 : mInvoice.getType())   // 发票信息
                .params("head", mInvoice == null ? -1 : mInvoice.getHead())   // 发票信息
                .params("company_name", mInvoice == null ? "" : mInvoice.getCompanyName())   // 发票信息
                .params("user_code", mInvoice == null ? "" : mInvoice.getUserCode())   // 发票信息
                .params("goods_type", mInvoice == null ? -1 : mInvoice.getGoodsType())   // 发票信息
                .params("email", mInvoice == null ? "" : mInvoice.getEmail())   // 发票信息
                .params("phone_num", mInvoice == null ? "" : mInvoice.getPhoneNum())   // 发票信息
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
                            EventBus.getDefault().post(new UpdateShoppingCart());
                            CommitOrder commitOrder = response.body().data;
                            if (commitOrder.getIsPay() == 1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderNo", commitOrder.getOrderNo());
                                bundle.putInt("buyType",buyType);
                                RxActivityTool.skipActivity(mContext, OrderSettlementActivity.class, bundle);
                                ConfirmOrderActivity.this.finish();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("payResult", true);
                                bundle.putInt("buyType",buyType);
                                RxActivityTool.skipActivity(mContext, PayResultActivity.class, bundle);
                                ConfirmOrderActivity.this.finish();
                            }
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

    /**
     * 加减商品数量,普通购买直接加减，购物车需要掉购物车加减商品数量接口
     *
     * @param flag     0 减   1加
     * @param position
     */
    private void subtractOrPlus(int flag, int position) {
        if (buyType == BuyType.SHOPPING_CART_BUY_NOW || buyType == BuyType.SHOPPING_CART_WECHAT_GIFT_GIVING) {
            updateShoppingCartCount(flag, position);
        } else {
            ConfirmOrder.Goods goods = mAdapter.getItem(position);
            int count = goods.getCount();
            if (flag == 0) {  // 减
                count--;
                if (count < 1) {
                    count = 1;
                    showToast("不能再减啦~");
                }
            } else {
                count++;
            }
            goods.setCount(count);
            mAdapter.notifyDataSetChanged();
            handlePrice();
        }
    }

    /**
     * 更新购物车数量
     *
     * @param flag     0 减商品数量，1 加商品
     * @param position adapter位置
     */
    private void updateShoppingCartCount(int flag, int position) {
        final ConfirmOrder.Goods goods = mAdapter.getItem(position);
        int count = goods.getCount();
        // 减商品数量
        if (flag == 0) {
            count--;
            if (count < 1) {
                showToast("不能再减啦~");
                return;
            }
        } else {
            count++;
        }

        final int finalCount = count;
        OkGo.<AMBaseDto<Empty>>get(Constants.updateShoppingCartGiftCountUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_cart_id", goods.getCartId())
                .params("goods_cart_counts", count)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        if (response.body().code == 0) {
                            goods.setCount(finalCount);
                            mAdapter.notifyDataSetChanged();
                            handlePrice();
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    private void handlePrice() {
        // 商品合计金额
        double totalAmount = 0.00;
        // 积分金额
        double pointAmount = 0.00;
        // 实付金额
        double realPayAmount = 0.00;
        List<ConfirmOrder.Goods> mList = mAdapter.getData();
        for (ConfirmOrder.Goods model : mList) {
            totalAmount = RxBigDecimalTool.add(totalAmount, Double.valueOf(model.getSalePrice()) * model.getCount());
        }
        mTvGoodsTogetherAmount.setText("¥" + RxBigDecimalTool.toDecimal(totalAmount, 2));
        // 是否可以抵扣积分
        if (mIvIntegralSwitch.isSelected() && point > 0) {
            pointAmount = point;
            // 金额大于积分
            if (totalAmount > pointAmount) {
                mTvPointsDeduction.setText("¥" + RxBigDecimalTool.toDecimal(pointAmount, 2));
                realPayAmount = RxBigDecimalTool.sub(totalAmount, pointAmount);
            } else { // 积分大于金额
                mTvPointsDeduction.setText("¥" + RxBigDecimalTool.toDecimal(totalAmount, 2));
                realPayAmount = 0.00;
            }
        } else {
            realPayAmount = totalAmount;
            if (point > 0) {
                mTvPointsDeduction.setText("未使用积分");
            }
        }
        // 实际支付金额
        mTvActuallyPaid.setText("¥ " + RxBigDecimalTool.toDecimal(realPayAmount, 2));
    }
}
