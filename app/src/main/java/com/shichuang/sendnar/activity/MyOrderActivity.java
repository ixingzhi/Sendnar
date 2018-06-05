package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxTabLayoutTool;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.MyOrderAdapter;
import com.shichuang.sendnar.adapter.RecommendGiftsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.OrderStatus;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ActionList;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.entify.MyOrder;
import com.shichuang.sendnar.entify.User;
import com.shichuang.sendnar.event.UpdateLoginStatus;
import com.shichuang.sendnar.event.UpdateOrderEvent;
import com.shichuang.sendnar.event.UpdateShoppingCart;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 * Created by Administrator on 2018/4/22.
 */

public class MyOrderActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // 商品订单
    private RecyclerView mRecyclerView;
    private MyOrderAdapter mAdapter;
    // 推荐商品
    private RecyclerView mRecommendRecyclerView;
    private RecommendGiftsAdapter mRecommendAdapter;
    private RxEmptyLayout mEmptyLayout;
    // 商品分页
    private int pageSize = 10;
    private int pageIndex = 1;
    // 推荐分页
    private int rPageSize = 10;
    private int rPageIndex = 1;

    //private int orderCount = 0;
    private int orderStatus;
    private int orderCount = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderStatus = getIntent().getIntExtra("orderStatus", OrderStatus.ALL);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"), orderStatus == OrderStatus.ALL ? true : false);
        mTabLayout.addTab(mTabLayout.newTab().setText("待付款"), orderStatus == OrderStatus.WAIT_PAYMENT ? true : false);
        mTabLayout.addTab(mTabLayout.newTab().setText("待发货"), orderStatus == OrderStatus.WAIT_WAIT_DELIVERY ? true : false);
        mTabLayout.addTab(mTabLayout.newTab().setText("待收货"), orderStatus == OrderStatus.DELIVERED ? true : false);
        RxTabLayoutTool.setIndicator(mContext,mTabLayout,8,8);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        initOrderRecyclerView();
        initRecommendRecyclerView();
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        EventBus.getDefault().register(this);
    }

    private void initOrderRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyOrderAdapter(null);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRecommendRecyclerView() {
        mRecommendRecyclerView = mContentView.findViewById(R.id.recycler_view_recommend);
        mRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecommendAdapter = new RecommendGiftsAdapter();
        mRecommendAdapter.setPreLoadNumber(2);
        mRecommendAdapter.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_my_order_empty, (ViewGroup) findViewById(android.R.id.content), false));
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
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
                switch (tab.getPosition()) {
                    case 0:
                        orderStatus = OrderStatus.ALL;
                        break;
                    case 1:
                        orderStatus = OrderStatus.WAIT_PAYMENT;
                        break;
                    case 2:
                        orderStatus = OrderStatus.WAIT_WAIT_DELIVERY;
                        break;
                    case 3:
                        orderStatus = OrderStatus.DELIVERED;
                        break;
                    default:
                        break;
                }
                refresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mRecommendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_add_shopping_cart) {
                    addShoppingCart(mRecommendAdapter.getData().get(position).getId());
                }
            }
        });
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                refresh();
            }
        });
    }

    @Override
    public void initData() {
        refresh();
        getRecommendGiftData();
    }

    private void refresh() {
        pageIndex = 1;
        orderCount = 0;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getOrderData();
            }
        });
    }

    private void loadMore() {
        getOrderData();
    }

    private void getOrderData() {
        OkGo.<AMBaseDto<MyOrder>>get(Constants.myOrderUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("order_status", orderStatus)
                .execute(new NewsCallback<AMBaseDto<MyOrder>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MyOrder>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MyOrder>> response) {
                        if (response.body().code == 0) {
                            MyOrder table = response.body().data;
                            setData(table);
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                mEmptyLayout.hide();
                                if (orderCount < table.getRecordCount()) {
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
                    public void onError(Response<AMBaseDto<MyOrder>> response) {
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

    private void setData(MyOrder data) {
        List<MyOrder> mList = new ArrayList<>();
        List<MyOrder.OrderInfo> orderList = data.getRows();
        orderCount += orderList.size();
        for (int i = 0; i < orderList.size(); i++) {
            List<MyOrder.OrderInfo.GoodsInfo> goodsList = orderList.get(i).getGoodsInfoList();
            // 头部
            MyOrder myHeaderOrder = new MyOrder();
            myHeaderOrder.setItemType(MyOrder.ORDER_HEADER);
            myHeaderOrder.setOrderPosition(i);
            myHeaderOrder.setRecordCount(data.getRecordCount());
            myHeaderOrder.setRows(data.getRows());
            mList.add(myHeaderOrder);
            // 体部
            for (int j = 0; j < goodsList.size(); j++) {
                MyOrder myBodyOrder = new MyOrder();
                myBodyOrder.setItemType(MyOrder.ORDER_BODY);
                myBodyOrder.setOrderPosition(i);
                myBodyOrder.setRecordCount(data.getRecordCount());
                myBodyOrder.setRows(data.getRows());
                myBodyOrder.getRows().get(i).setGoodsPosition(j);
                mList.add(myBodyOrder);
            }
            // 脚部
            MyOrder myFootOrder = new MyOrder();
            myFootOrder.setItemType(MyOrder.ORDER_FOOT);
            myFootOrder.setOrderPosition(i);
            myFootOrder.setRecordCount(data.getRecordCount());
            myFootOrder.setRows(data.getRows());
            mList.add(myFootOrder);
        }
        // 判断是否显示推荐商品
        if (mList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecommendRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mRecommendRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(mList);
            mRecyclerView.scrollToPosition(0);
        } else {
            mAdapter.addData(mList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateOrderEvent event) {/* Do something */
        if (event != null) {
            refresh();
        }
    }

    /**
     * 获得推荐商品
     */
    private void getRecommendGiftData() {
        OkGo.<AMBaseDto<GoodsList>>get(Constants.recommendGoodsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("pageSize", rPageSize)
                .params("pageIndex", rPageIndex)
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
                                    rPageIndex++;
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
                            //showToast(response.body().msg);
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
        mRecommendAdapter.addData(data.getRows());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
