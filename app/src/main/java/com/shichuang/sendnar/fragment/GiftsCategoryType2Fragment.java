package com.shichuang.sendnar.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.GiftsDetailsActivity;
import com.shichuang.sendnar.adapter.GiftsCategoryType1Adapter;
import com.shichuang.sendnar.adapter.GiftsCategoryType2Adapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.GiftsDetailsType;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ActionList;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.entify.GiftsCategoryType2;
import com.shichuang.sendnar.tool.BannerImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GiftsCategoryType2Fragment extends BaseFragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mHeaderView;
    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private GiftsCategoryType2Adapter mAdapter;

    private int priceTypeId;
    private int pageSize = 10;
    private int pageIndex = 1;

    private ExchangeGift exchangeGift;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gifts_category_type_2;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        priceTypeId = getArguments().getInt("priceTypeId");
        exchangeGift = (ExchangeGift) getArguments().getSerializable("exchangeGift");

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_gifts_category_type_2_header, (ViewGroup) mContentView.findViewById(android.R.id.content), false);
        initBanner();
        initRecyclerView();
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

            }
        });
//        // 设置比例尺寸 300 * 750
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBanner.getLayoutParams();
//        // 根据比例获取高度
//        int height = (int) ((float) 300 * RxScreenTool.getDisplayMetrics(mContext).widthPixels / 750);
//        params.height = height;
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new GiftsCategoryType2Adapter();
        mAdapter.setExchangeGift(exchangeGift);
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
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", Integer.valueOf(mAdapter.getData().get(position).getActionItemId()));
                bundle.putInt("operationType", GiftsDetailsType.ACTION_GOODS_DETAILS);
                bundle.putBoolean("isHideAddShoppingCart", true);
                bundle.putSerializable("exchangeGift", exchangeGift);
                RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
    }

    @Override
    public void initData() {
        refresh();
    }

    private void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    private void loadMore() {
        getData();
    }

    private void getData() {
        OkGo.<AMBaseDto<GiftsCategoryType2>>get(Constants.giftsCategoryType2Url)
                .tag(mContext)
                .params("type_id", priceTypeId)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<GiftsCategoryType2>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<GiftsCategoryType2>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<GiftsCategoryType2>> response) {
                        if (response.body().code == 0) {
                            // 商品列表
                            GiftsCategoryType2.GiftsList table = response.body().data.getGoodsList();
                            setData(table.getRows());
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
                                        //showToast("没有更多数据");
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                        //mAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                            // banner 图数据
                            List<GiftsCategoryType2.PicList> picList = response.body().data.getPicList();
                            if (picList != null && picList.size() > 0) {
                                List<String> bannerList = new ArrayList<>();
                                for (GiftsCategoryType2.PicList model : picList) {
                                    bannerList.add(Constants.MAIN_ENGINE_PIC + model.getPic());
                                }
                                mBanner.update(bannerList);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<GiftsCategoryType2>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<GiftsCategoryType2.GiftsList.GiftsListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
