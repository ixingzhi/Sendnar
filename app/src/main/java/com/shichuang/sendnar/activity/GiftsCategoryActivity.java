package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.GiftsListAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.GiftsDetailsType;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.entify.FestivalBannerAndGiftBag;
import com.shichuang.sendnar.entify.GiftsCategoryType1;
import com.shichuang.sendnar.entify.Home;
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
 * 礼品分类
 * Created by Administrator on 2018/4/18.
 */

public class GiftsCategoryActivity extends BaseActivity {
    private RxTitleBar mTitleBar;
    private TabLayout mTabLayout;
    private Banner mBanner;
    private EditText mEtGoodsSearch;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mHeaderView;
    private RecyclerView mRecyclerView;
    private GiftsListAdapter mAdapter;


    private int pageSize = 10;
    private int pageIndex = 1;

    private List<GiftsCategoryType1> giftsCategoryTypeList;
    // 类型Id（专区、礼品）
    private int typeId;
    // (价格分类，扶贫节日)
    private int priceTypeId;
    // 送礼对象ID
    private int giftObjectId;
    // 是不是转赠商品
    private ExchangeGift exchangeGift;
    // Banner数据
    private List<Home.Banner> mBannerDataList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gifts_category;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        typeId = getIntent().getIntExtra("typeId", 0);
        priceTypeId = getIntent().getIntExtra("priceTypeId", 0);
        giftObjectId = getIntent().getIntExtra("giftObjectId", 0);
        exchangeGift = (ExchangeGift) getIntent().getSerializableExtra("exchangeGift");
        mBannerDataList = (List<Home.Banner>) getIntent().getSerializableExtra("bannerData");
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mTitleBar = view.findViewById(R.id.title_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_gifts_category_header, (ViewGroup) findViewById(android.R.id.content), false);
        mEtGoodsSearch = mHeaderView.findViewById(R.id.et_goods_search);
        initBanner();
        initRecyclerView();

        if (typeId == 13) {
            mTitleBar.setTitle("专区");
        } else {
            mTitleBar.setTitle("礼品");
        }
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new GiftsListAdapter();
        mAdapter.setExchangeGift(exchangeGift);
        mAdapter.setPreLoadNumber(2);
        mAdapter.addHeaderView(mHeaderView);
        mRecyclerView.setAdapter(mAdapter);
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
                            SinglePage.getInstance().toPage(mContext, "关于送哪儿", SinglePage.COMPANY_PROFILE, bannerData.getPicValueParameter());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        // 设置比例尺寸 300 * 750
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBanner.getLayoutParams();
        // 根据比例获取高度
        int height = (int) ((float) 300 * RxScreenTool.getDisplayMetrics(mContext).widthPixels / 750);
        params.height = height;
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
                bundle.putInt("id", mAdapter.getData().get(position).getId());
                bundle.putInt("operationType", GiftsDetailsType.GOODS_DETAILS);
                bundle.putSerializable("exchangeGift", exchangeGift);
                RxActivityTool.skipActivity(mContext, GiftsDetailsActivity.class, bundle);
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
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (giftsCategoryTypeList != null && tab.getPosition() <= giftsCategoryTypeList.size()) {
                    giftObjectId = giftsCategoryTypeList.get(tab.getPosition()).getId();
                    refresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mEtGoodsSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String txt = mEtGoodsSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(txt)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("typeId", typeId);
                        bundle.putInt("giftObjectId", giftObjectId);
                        bundle.putString("content", txt);
                        RxActivityTool.skipActivity(mContext, SearchResultActivity.class, bundle);
                    } else {
                        showToast("请输入搜索内容");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        // 获取分类成功后获取商品列表
        getCategory();
        // 获取banner和礼包数据
        //getBannerAndGiftBagData();
        getBannerData();
    }

    private void getBannerData() {
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
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Home>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 获取分类
     */
    private void getCategory() {
        OkGo.<AMBaseDto<List<GiftsCategoryType1>>>get(Constants.giftsCategoryType1Url)
                .tag(mContext)
                .params("platform_goods_category_id", priceTypeId)
                .execute(new NewsCallback<AMBaseDto<List<GiftsCategoryType1>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<GiftsCategoryType1>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<GiftsCategoryType1>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            giftsCategoryTypeList = response.body().data;
                            // 添加到TabLayout中，会默认执行第一个onTabSelected
                            for (GiftsCategoryType1 model : giftsCategoryTypeList) {
                                // 首页进入
                                if (giftObjectId == 0) {
                                    mTabLayout.addTab(mTabLayout.newTab().setText(model.getName()));
                                } else {  // 分类进入
                                    if (giftObjectId == model.getId()) {
                                        mTabLayout.addTab(mTabLayout.newTab().setText(model.getName()), true);
                                    } else {
                                        mTabLayout.addTab(mTabLayout.newTab().setText(model.getName()), false);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<GiftsCategoryType1>>> response) {
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
     * 获取banner和礼包数据
     */
    private void getBannerAndGiftBagData() {
        OkGo.<AMBaseDto<FestivalBannerAndGiftBag>>get(Constants.actionCarouselListUrl)
                .tag(mContext)
                .params("type_id", typeId)
                //.params("type_id", 12)
                .execute(new NewsCallback<AMBaseDto<FestivalBannerAndGiftBag>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<FestivalBannerAndGiftBag>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<FestivalBannerAndGiftBag>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            // Banner图
                            List<FestivalBannerAndGiftBag.PicList> picList = response.body().data.getPicList();
                            if (picList != null && picList.size() > 0) {
                                List<String> bannerList = new ArrayList<>();
                                for (FestivalBannerAndGiftBag.PicList model : picList) {
                                    bannerList.add(Utils.getSingleImageUrlByImageUrls(model.getPic()));
                                }
                                mBanner.update(bannerList);
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
                getGoodsListData();
            }
        });
    }

    private void loadMore() {
        getGoodsListData();
    }

    /**
     * 获取商品列表
     */
    private void getGoodsListData() {
        OkGo.<AMBaseDto<GoodsList>>get(Constants.goodsListUrl)
                .tag(mContext)
                .params("type_id", typeId)  // 活动类型
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("festival_id", giftObjectId)   // 送礼对象
                .params("goods_category_id", priceTypeId)  // 价格分类id
                .execute(new NewsCallback<AMBaseDto<GoodsList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<GoodsList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<GoodsList>> response) {
                        if (response.body().code == 0) {
                            GoodsList table = response.body().data;
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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


    private void setData(List<GoodsList.GoodsListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
            mRecyclerView.scrollToPosition(0);
        } else {
            mAdapter.addData(data);
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
