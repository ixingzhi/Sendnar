package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.FestivalAdapter;
import com.shichuang.sendnar.adapter.GiftBagAdapter;
import com.shichuang.sendnar.adapter.GiftsListAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.GiftsDetailsType;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.FestivalBannerActivitiesDetails;
import com.shichuang.sendnar.entify.FestivalList;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.entify.FestivalBannerAndGiftBag;
import com.shichuang.sendnar.event.UpdateShoppingCart;
import com.shichuang.sendnar.tool.BannerImageLoader;
import com.shichuang.sendnar.widget.RxTitleBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 节日
 * Created by Administrator on 2018/4/20.
 */

public class FestivalActivity extends BaseActivity {
    // 往期活动
    private static final int PAST_ACTIVITIES = 0x11;
    private Banner mBanner;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mHeaderView;
    // 礼包
    private RecyclerView mGiftBagRecyclerView;
    private GiftBagAdapter mGiftBagAdapter;
    // 节日
    private RecyclerView mRecyclerView;
    private FestivalAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private int typeId = -1;
    private int pageSize = 10;
    private int pageIndex = 1;
    private int operationType = 1;  // 1-当前进行中的活动 2-往期活动
    private int festivalId = -1;
    private int actionId = -1;

    private List<FestivalBannerAndGiftBag.PicList> mBannerDataList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_festival;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        typeId = getIntent().getIntExtra("typeId", typeId);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_festival_header, (ViewGroup) findViewById(android.R.id.content), false);
        initBanner();
        initGiftBagRecyclerView();
        initRecyclerView();
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
    }


    private void initBanner() {
        mBanner = mHeaderView.findViewById(R.id.banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new BannerImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerDataList != null && position < mBannerDataList.size()) {
                    skipActionPage(mBannerDataList.get(position).getId());
                }
            }
        });
        // 设置比例尺寸 300 * 750
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        // 根据比例获取高度
        int height = (int) ((float) 300 * RxScreenTool.getDisplayMetrics(mContext).widthPixels / 750);
        params.height = height;
    }

    private void initGiftBagRecyclerView() {
        mGiftBagRecyclerView = mHeaderView.findViewById(R.id.recycler_view_gift_bag);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mGiftBagRecyclerView.setLayoutManager(mLayoutManager);
        mGiftBagAdapter = new GiftBagAdapter();
        mGiftBagAdapter.setPreLoadNumber(2);
        mGiftBagRecyclerView.setAdapter(mGiftBagAdapter);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FestivalAdapter();
        mAdapter.setPreLoadNumber(2);
        mAdapter.addHeaderView(mHeaderView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                Bundle bundle = new Bundle();
                bundle.putInt("typeId", typeId);
                RxActivityTool.skipActivityForResult(FestivalActivity.this, PastActivitiesActivity.class, bundle, PAST_ACTIVITIES);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 添加到购物车
                if (view.getId() == R.id.iv_add_shopping_cart) {
                    if (Utils.isLogin(mContext)) {
                        addShoppingCart(mAdapter.getData().get(position).getId());
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", mAdapter.getData().get(position).getId());
                bundle.putInt("operationType", GiftsDetailsType.ACTION_GOODS_DETAILS);
                RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle);
            }
        });
        mGiftBagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", mGiftBagAdapter.getData().get(position).getId());
                bundle.putInt("operationType", GiftsDetailsType.GIFT_BAG_DETAILS);
                RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle);
            }
        });
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                getBannerAndGiftBagData();
                refresh();
            }
        });
    }

    @Override
    public void initData() {
        getBannerAndGiftBagData();
        refresh();
    }

    /**
     * 获取banner和礼包数据
     */
    private void getBannerAndGiftBagData() {
        OkGo.<AMBaseDto<FestivalBannerAndGiftBag>>get(Constants.actionCarouselList2Url)
                .tag(mContext)
                .params("operation_type", operationType)   // 操作类型   1-当前进行中的活动 2-往期活动
                .params("type_id", typeId)
                .params("action_id", actionId)
                .execute(new NewsCallback<AMBaseDto<FestivalBannerAndGiftBag>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<FestivalBannerAndGiftBag>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<FestivalBannerAndGiftBag>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            // Banner图
                            mBannerDataList = response.body().data.getPicList();
                            if (mBannerDataList != null && mBannerDataList.size() > 0) {
                                List<String> bannerList = new ArrayList<>();
                                for (FestivalBannerAndGiftBag.PicList model : mBannerDataList) {
                                    bannerList.add(Utils.getSingleImageUrlByImageUrls(model.getPic()));
                                }
                                mBanner.update(bannerList);
                            }
                            // 礼包
                            List<FestivalBannerAndGiftBag.PackageList> packageList = response.body().data.getPackageList();
                            if (packageList != null) {
                                mGiftBagAdapter.replaceData(packageList);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<FestivalBannerAndGiftBag>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    private void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                //  往期活动
                getActionData();
            }
        });
    }

    private void loadMore() {
        getActionData();
    }

    /**
     * 活动商品
     */
    private void getActionData() {
        OkGo.<AMBaseDto<FestivalList>>get(Constants.actionList2Url)
                .tag(mContext)
                .params("operation_type", operationType)  // 操作类型//1-当前进行中的活动 2-往期活动
                .params("type_id", typeId)
                .params("festival_id", festivalId)  // 送礼对象
                .params("action_id", actionId)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<FestivalList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<FestivalList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<FestivalList>> response) {
                        if (response.body().code == 0) {
                            FestivalList table = response.body().data;
                            setData(table.getRows());
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                mEmptyLayout.hide();
                                if (mAdapter.getData().size() < table.getRecordCount()) {
                                    pageIndex++;
                                    mAdapter.loadMoreComplete();
                                    mAdapter.setEnableLoadMore(true);
                                } else {
                                    if (table.getRecordCount() < pageSize) {
                                        mAdapter.loadMoreEnd(true);
                                        //showToast("没有更多数据");
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                        //mAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                mEmptyLayout.hide();
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<FestivalList>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<FestivalList.FestivalListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAST_ACTIVITIES && resultCode == RESULT_OK) {
            operationType = 2;
            actionId = data.getIntExtra("actionId", 0);
            typeId = data.getIntExtra("typeId", 0);
            getBannerAndGiftBagData();
            refresh();
        }
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
                .params("goods_id", -1)
                .params("goods_cart_counts", 1)
                .params("action_item_id", id)
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


    private void skipActionPage(int actionId) {
        OkGo.<AMBaseDto<FestivalBannerActivitiesDetails>>get(Constants.actionDetailsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("action_id", actionId)
                .execute(new NewsCallback<AMBaseDto<FestivalBannerActivitiesDetails>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<FestivalBannerActivitiesDetails>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<FestivalBannerActivitiesDetails>> response) {
                        dismissLoading();
                        if (response.body().code == 0 && response.body().data != null) {
                            FestivalBannerActivitiesDetails.ActionDetail actionDetail = response.body().data.getActionDetail();
                            WebPageActivity.newInstance(mContext, "关于送哪儿", Constants.MAIN_ENGINE_PIC + actionDetail.getH5Url());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<FestivalBannerActivitiesDetails>> response) {
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
