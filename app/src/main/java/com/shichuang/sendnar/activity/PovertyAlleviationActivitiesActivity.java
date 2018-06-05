package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.PovertyAlleviationActivitiesAdapter;
import com.shichuang.sendnar.adapter.PovertyAlleviationActivitiesNowAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ActionList;

import java.util.List;

/**
 * 扶贫活动
 * Created by xiedd on 2018/4/17.
 */

public class PovertyAlleviationActivitiesActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mHeaderView;
    // 进行中的活动
    private RecyclerView mActionNowRecyclerView;
    private PovertyAlleviationActivitiesNowAdapter mActionNowAdapter;
    // 往期活动
    private RecyclerView mRecyclerView;
    private PovertyAlleviationActivitiesAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private int pageSize = 10;
    private int pageIndex = 1;
    private int typeId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_poverty_alleviation_activities;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        typeId = getIntent().getIntExtra("typeId", 0);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_poverty_alleviation_activities_header, (ViewGroup) findViewById(android.R.id.content), false);
        initActionNowRecyclerView();
        initRecyclerView();
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
    }

    /**
     * 进行中的活动
     */
    private void initActionNowRecyclerView() {
        mActionNowRecyclerView = mHeaderView.findViewById(R.id.recycler_view_action_now);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mActionNowRecyclerView.setLayoutManager(mLayoutManager);
        mActionNowAdapter = new PovertyAlleviationActivitiesNowAdapter();
        mActionNowAdapter.setPreLoadNumber(2);
        mActionNowRecyclerView.setAdapter(mActionNowAdapter);
    }

    /**
     * 往期活动
     */
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new PovertyAlleviationActivitiesAdapter();
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
                bundle.putInt("id", mAdapter.getData().get(position).getId());
                bundle.putBoolean("isEnd", true);
                RxActivityTool.skipActivity(mContext, PovertyAlleviationActivitiesDetailsActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mActionNowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", mActionNowAdapter.getData().get(position).getId());
                bundle.putBoolean("isEnd", false);
                RxActivityTool.skipActivity(mContext, PovertyAlleviationActivitiesDetailsActivity.class, bundle);
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
    }

    private void getNowActionData() {
        OkGo.<AMBaseDto<List<ActionList.ActionListModel>>>get(Constants.nowActionUrl)
                .tag(mContext)
                .params("type_id", typeId)
                .execute(new NewsCallback<AMBaseDto<List<ActionList.ActionListModel>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<ActionList.ActionListModel>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<ActionList.ActionListModel>>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.size() > 0) {
                            mActionNowAdapter.replaceData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<ActionList.ActionListModel>>> response) {
                        super.onError(response);
                        //showToast("数据异常：" + response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void refresh() {
        // 进行中的活动
        getNowActionData();
        pageIndex = 1;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                //  往期活动
                getPastActionData();
            }
        });
    }

    private void loadMore() {
        getPastActionData();
    }

    private void getPastActionData() {
        OkGo.<AMBaseDto<ActionList>>get(Constants.pastActionListUrl)
                .tag(mContext)
                .params("type_id", typeId)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<ActionList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ActionList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ActionList>> response) {
                        if (response.body().code == 0) {
                            ActionList table = response.body().data;
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
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ActionList>> response) {
                        super.onError(response);
                        showToast("数据异常：" + response.getException().getMessage());
                        //mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<ActionList.ActionListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
