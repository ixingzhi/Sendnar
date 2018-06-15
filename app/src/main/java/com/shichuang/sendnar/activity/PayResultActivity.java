package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.MainActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.RecommendGiftsAdapter;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.event.UpdateShoppingCart;

import org.greenrobot.eventbus.EventBus;

/**
 * 支付成功
 * Created by Administrator on 2018/4/19.
 */

public class PayResultActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mRlTitleBar;
    private ImageView mIvPayResult;
    private TextView mTvPayResult;
    private RecyclerView mRecyclerView;
    private RecommendGiftsAdapter mAdapter;

    private int pageSize = 10;
    private int pageIndex = 1;
    private boolean payResult;
    // 购买类型
    private int buyType;
    private String orderNo;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_pay_result;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        payResult = getIntent().getBooleanExtra("payResult", false);
        buyType = getIntent().getIntExtra("buyType", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        mRlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar);
        // 获取状态栏的高度
        int statusBarHeight = RxStatusBarTool.getStatusBarHeight(mContext);
        // 动态修改高度
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRlTitleBar.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        mIvPayResult = (ImageView) findViewById(R.id.iv_pay_result);
        mTvPayResult = (TextView) findViewById(R.id.tv_pay_result);

        if (payResult) {
            mIvPayResult.setImageResource(R.drawable.ic_pay_success);
            mTvPayResult.setText("支付成功");
        } else {
            mIvPayResult.setImageResource(R.drawable.ic_pay_fail);
            mTvPayResult.setText("支付失败");
        }

        // 如果是微信送礼，直接跳转我的物品
        if (buyType == BuyType.WECHAT_GIFT_GIVING || buyType == BuyType.SHOPPING_CART_WECHAT_GIFT_GIVING) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", orderNo);
                    RxActivityTool.skipActivity(mContext, MyItemsActivity.class, bundle);
                    PayResultActivity.this.finish();
                }
            }, 800);
        } else {
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
//        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
//        mLayoutManager.setScrollEnabled(false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecommendGiftsAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(this);
        findViewById(R.id.tv_check_order).setOnClickListener(this);
        findViewById(R.id.tv_back_home).setOnClickListener(this);
        if (buyType == BuyType.BUY_NOW || buyType == BuyType.SHOPPING_CART_BUY_NOW) {
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.iv_add_shopping_cart) {
                        addShoppingCart(mAdapter.getData().get(position).getId());
                    }
                }
            });
        }
    }

    @Override
    public void initData() {
        // 非微信送礼，加载
        if (buyType == BuyType.BUY_NOW || buyType == BuyType.SHOPPING_CART_BUY_NOW) {
            getRecommendGiftData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                RxActivityTool.finish(mContext);
                break;
            case R.id.tv_check_order:
                RxActivityTool.skipActivity(mContext, MyOrderActivity.class);
                RxActivityTool.finish(mContext);
                break;
            case R.id.tv_back_home:
                RxActivityTool.skipActivity(mContext, MainActivity.class);
                RxActivityTool.finish(mContext);
                break;
            default:
                break;
        }
    }

    /**
     * 获得推荐商品
     */
    private void getRecommendGiftData() {
        OkGo.<AMBaseDto<GoodsList>>get(Constants.recommendGoodsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<GoodsList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<GoodsList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<GoodsList>> response) {
                        if (response.body().code == 0) {
                            GoodsList table = response.body().data;
                            setRecommendData(table);
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                //mEmptyLayout.hide();
                                if (mAdapter.getData().size() < table.getRecordCount()) {
                                    pageIndex++;
                                    mAdapter.loadMoreComplete();
                                    mAdapter.setEnableLoadMore(true);
                                } else {
                                    if (table.getRecordCount() < pageSize) {
                                        mAdapter.loadMoreEnd(true);
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<GoodsList>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 绑定推荐数据
     *
     * @param data
     */
    private void setRecommendData(GoodsList data) {
        mAdapter.addData(data.getRows());
    }

    /**
     * 添加到购物车
     *
     * @param id
     */
    private void addShoppingCart(int id) {
        OkGo.<AMBaseDto<Empty>>get(Constants.addShoppingCartUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_id", id)
                .params("goods_cart_counts", 1)
                .params("action_item_id", -1)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            EventBus.getDefault().post(new UpdateShoppingCart());
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
}
