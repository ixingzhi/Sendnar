package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.GiftsListAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.GoodsList;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SearchResultActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private GiftsListAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private int typeId;
    private int giftObjectId;
    private String content;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        typeId = getIntent().getIntExtra("typeId", 0);
        giftObjectId = getIntent().getIntExtra("giftObjectId", 0);
        content = getIntent().getStringExtra("content");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        initRecyclerView();
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new GiftsListAdapter();
        mAdapter.setPreLoadNumber(2);
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
                //  往期活动
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
        OkGo.<AMBaseDto<GoodsList>>get(Constants.searchGoodsByGoodsNameUrl)
                .tag(mContext)
                .params("type_id", typeId)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("festival_id", giftObjectId)   // 送礼对象
                .params("goods_category_id", "")
                .params("Keyword", content)
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
                                mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<GoodsList>> response) {
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


    private void setData(List<GoodsList.GoodsListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
