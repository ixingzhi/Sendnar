package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.RecommendGiftsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.entify.Point;
import com.shichuang.sendnar.entify.Page;
import com.shichuang.sendnar.event.UpdateShoppingCart;

import org.greenrobot.eventbus.EventBus;

/**
 * 我的积分
 * Created by Administrator on 2018/4/23.
 */

public class MyIntegralActivity extends BaseActivity {
    private RelativeLayout mRlTitleBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvPoint;
    private RecyclerView mRecyclerView;
    private RecommendGiftsAdapter mAdapter;
    private View mHeaderView;

    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBarLightMode(this, true);
        return R.layout.activity_my_integral;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mRlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlTitleBar.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecommendGiftsAdapter();
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_my_integral_header, (ViewGroup) findViewById(android.R.id.content), false);
        mTvPoint = mHeaderView.findViewById(R.id.tv_point);
        mAdapter.addHeaderView(mHeaderView);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
        findViewById(R.id.ll_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RxActivityTool.skipActivity(mContext, IntegralGuideActivity.class);
                //getIntegralGuideData();
                SinglePage.getInstance().toPage(mContext,"积分指南",SinglePage.INTEGRAL_GUIDE,"");
            }
        });
        mHeaderView.findViewById(R.id.tv_integral_subsidiary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, IntegralSubsidiaryActivity.class);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPointData();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_add_shopping_cart) {
                    addShoppingCart(mAdapter.getData().get(position).getId());
                }
            }
        });
    }

    @Override
    public void initData() {
        getPointData();
        getRecommendGiftData();
    }

    private void getPointData() {
        OkGo.<AMBaseDto<Point>>get(Constants.myPointUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<Point>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Point>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Point>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            mTvPoint.setText(response.body().data.getPoint());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Point>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
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
     * 积分指南
     */
    private void getIntegralGuideData() {
        OkGo.<AMBaseDto<Page>>get(Constants.integralGuideUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<Page>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Page>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Page>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            WebPageActivity.newInstance(mContext, "积分指南", Constants.MAIN_ENGINE_PIC + response.body().data.getH5Url());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Page>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast(response.getException().getMessage());

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
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
