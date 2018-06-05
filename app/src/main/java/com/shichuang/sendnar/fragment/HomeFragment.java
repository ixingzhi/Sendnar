package com.shichuang.sendnar.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.FestivalActivity;
import com.shichuang.sendnar.activity.GiftsCategoryActivity;
import com.shichuang.sendnar.activity.GiftsDetailsActivity;
import com.shichuang.sendnar.activity.MessageActivity;
import com.shichuang.sendnar.activity.PovertyAlleviationActivitiesActivity;
import com.shichuang.sendnar.activity.PovertyAlleviationActivitiesDetailsActivity;
import com.shichuang.sendnar.adapter.HomeAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.GiftsDetailsType;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.UdeskHelper;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Home;
import com.shichuang.sendnar.interf.OnTabReselectListener;
import com.shichuang.sendnar.tool.BannerImageLoader;
import com.shichuang.sendnar.widget.VerticalSwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import cn.udesk.UdeskSDKManager;
import cn.udesk.activity.UdeskChatActivity;
import cn.udesk.messagemanager.UdeskMessageManager;
import cn.udesk.model.MsgNotice;

/**
 * Created by Administrator on 2018/4/16.
 */

public class HomeFragment extends BaseFragment  implements OnTabReselectListener {
    private RxEmptyLayout mEmptyLayout;
    private VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    private Banner mBanner;
    private View mHeaderView;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    // Banner数据
    private List<Home.Banner> mBannerDataList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        View mViewStatusBar = view.findViewById(R.id.view_status_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusBar.getLayoutParams();
        params.height = RxStatusBarTool.getStatusBarHeight(mContext);
        mEmptyLayout = view.findViewById(R.id.empty_layout);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_banner, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
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
                if (mBannerDataList != null && mBannerDataList.size() > 0) {
                    Home.Banner bannerData = mBannerDataList.get(position);
                    switch (bannerData.getPicValueType()) {
                        case 2:  // 商品id
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", Integer.valueOf(bannerData.getPicValueParameter()));
                            bundle.putInt("operationType", GiftsDetailsType.GOODS_DETAILS);
                            RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle);
                            break;
                        case 3:  // 活动id
                            if (bannerData.getTypeId() == 11) { // 扶贫
                                Bundle bundle1 = new Bundle();
                                bundle1.putInt("id", Integer.valueOf(bannerData.getPicValueParameter()));
                                bundle1.putBoolean("isEnd", false);
                                RxActivityTool.skipActivity(mContext, PovertyAlleviationActivitiesDetailsActivity.class, bundle1);
                            } else if (bannerData.getTypeId() == 12) { // 节日
                                Bundle bundle2 = new Bundle();
                                //bundle2.putInt("actionId", Integer.valueOf(bannerData.getPicValueParameter()));
                                bundle2.putInt("typeId", bannerData.getTypeId());
                                RxActivityTool.skipActivity(mContext, FestivalActivity.class, bundle2);
                            }
                            break;
                        case 4:  // 礼包id
                            Bundle bundle3 = new Bundle();
                            bundle3.putInt("id", Integer.valueOf(bannerData.getPicValueParameter()));
                            bundle3.putInt("operationType", GiftsDetailsType.GIFT_BAG_DETAILS);
                            RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle3);
                            break;
                        case 5:  // 单页
                            SinglePage.getInstance().toPage(mContext, "公司简介", SinglePage.COMPANY_PROFILE, bannerData.getPicValueParameter());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        // 设置比例尺寸 300 * 750
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBanner.getLayoutParams();
        // 根据比例获取高度
        int height = (int) ((float) 300 * RxScreenTool.getDisplayMetrics(mContext).widthPixels / 750);
        params.height = height;
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HomeAdapter();
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
                int id = mAdapter.getData().get(position).getId();
                int typeId = mAdapter.getData().get(position).getTypeId();
                Bundle bundle = new Bundle();
                bundle.putInt("typeId", typeId);
                switch (id) {
                    case 11:
                        RxActivityTool.skipActivity(mContext, PovertyAlleviationActivitiesActivity.class, bundle);
                        break;
                    case 12:
                        RxActivityTool.skipActivity(mContext, FestivalActivity.class, bundle);
                        break;
                    case 13:
                        RxActivityTool.skipActivity(mContext, GiftsCategoryActivity.class, bundle);
                        break;
                    case 14:
                        RxActivityTool.skipActivity(mContext, GiftsCategoryActivity.class, bundle);
                        break;
                    default:
                        break;
                }

            }
        });
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                refresh();
            }
        });
        // 客服
        mContentView.findViewById(R.id.ll_customer_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLogin(mContext)) {
                    UdeskHelper.entryChat(mContext);
                }
            }
        });
        mContentView.findViewById(R.id.ll_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLogin(mContext)) {
                    RxActivityTool.skipActivity(mContext, MessageActivity.class);
                }
            }
        });
    }

    @Override
    public void initData() {
        refresh();
    }

    private void refresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getHomeData();
            }
        });
    }

    private void getHomeData() {
        OkGo.<AMBaseDto<Home>>get(Constants.homeUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<Home>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Home>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Home>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            Home home = response.body().data;
                            if (home.getCarouselpics() != null) {
                                mBannerDataList = home.getCarouselpics();
                                List<String> bannerUrlList = new ArrayList<>();
                                for (Home.Banner model : mBannerDataList) {
                                    bannerUrlList.add(Constants.MAIN_ENGINE_PIC + model.getPic());
                                }
                                mBanner.update(bannerUrlList);
                            }
                            if (home.getListpics() != null) {
                                mAdapter.replaceData(home.getListpics());
                            }
                            mEmptyLayout.hide();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Home>> response) {
                        super.onError(response);
                        showToast("数据异常：" + response.getException().getMessage());
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onTabReselect() {
        mRecyclerView.scrollToPosition(0);
        refresh();
    }
}
