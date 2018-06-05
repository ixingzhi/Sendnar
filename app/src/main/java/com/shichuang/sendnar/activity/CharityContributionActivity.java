package com.shichuang.sendnar.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.CharityContributionAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.CharityContribution;
import com.shichuang.sendnar.entify.EarningsInformationThisMonth;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.event.MessageEvent;
import com.shichuang.sendnar.widget.PromptDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 排行榜（只有发起人才可以设置 设为合伙人）
 * Created by Administrator on 2018/4/23.
 */

public class CharityContributionActivity extends BaseActivity {
    private RxEmptyLayout mEmptyLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CharityContributionAdapter mAdapter;

    private TextView mTvThisMonthAmount;
    private TextView mTvAggregateAmount;
    private int actionType;

    private int pageSize = 10;
    private int pageIndex = 1;
    private EarningsInformationThisMonth mThisMonthAmountData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_charity_contribution;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        actionType = getIntent().getIntExtra("actionType", 0);
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        initRecyclerView();
        mTvThisMonthAmount = (TextView) findViewById(R.id.tv_this_month_amount);
        mTvAggregateAmount = (TextView) findViewById(R.id.tv_aggregate_amount);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CharityContributionAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
        // 设置是否可以侧滑，只有发起人可以设置消费者成为合伙人
        mAdapter.setCanRightSwipe(actionType == 3);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.iv_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThisMonthAmountData != null) {
                    SimpleDateFormat fromSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat toSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

                    String firstDay = RxTimeTool.date2String(RxTimeTool.string2Date(mThisMonthAmountData.getFirstDay(), fromSimpleDateFormat), toSimpleDateFormat);
                    String startDay = RxTimeTool.date2String(RxTimeTool.string2Date(mThisMonthAmountData.getStartDay(), fromSimpleDateFormat), toSimpleDateFormat);
                    String today = RxTimeTool.date2String(RxTimeTool.string2Date(mThisMonthAmountData.getToday(), fromSimpleDateFormat), toSimpleDateFormat);

                    View view = LayoutInflater.from(mContext).inflate(R.layout.layout_ranking_amount, null);
                    ((TextView) view.findViewById(R.id.tv_this_month_amount_start_stop_time)).setText(firstDay + "-" + today);
                    ((TextView) view.findViewById(R.id.tv_accumulative_total_amount_start_stop_time)).setText(startDay + "-" + today);
                    PromptDialog mDialog = new PromptDialog(mContext, view);
                    mDialog.show();
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.right) {
                    new AlertDialog.Builder(mContext).setMessage("设置为合伙人？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setPartner(mAdapter.getItem(position).getId());
                        }
                    }).setNegativeButton("取消", null).show();
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
        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
        refresh();
        getEarningsInformationThisMonth();
    }

    private void refresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    private void getData() {
        OkGo.<AMBaseDto<CharityContribution>>get(Constants.rankUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("action_type", actionType)   // 1  是合伙人查所有消费者给我的收益 2  是发起人查所有合伙人给我的收益  3   是发起人查所有消费者给我的收益
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<CharityContribution>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<CharityContribution>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<CharityContribution>> response) {
                        if (response.body().code == 0) {
                            CharityContribution table = response.body().data;
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
                    public void onError(Response<AMBaseDto<CharityContribution>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<CharityContribution.CharityContributionModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }

    /**
     * 本月收益信息
     */
    private void getEarningsInformationThisMonth() {
        OkGo.<AMBaseDto<EarningsInformationThisMonth>>get(Constants.getEarningsInformationThisMonthUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("action_type", actionType)
                .execute(new NewsCallback<AMBaseDto<EarningsInformationThisMonth>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<EarningsInformationThisMonth>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<EarningsInformationThisMonth>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            mThisMonthAmountData = response.body().data;
                            handleData();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<EarningsInformationThisMonth>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData() {
        EarningsInformationThisMonth.TotalAmount totalAmount = mThisMonthAmountData.getTotalAmount();
        if (totalAmount != null) {
            mTvThisMonthAmount.setText("本月金额：¥" + RxBigDecimalTool.toDecimal(totalAmount.getThisMonthRevenue(), 2));
            mTvAggregateAmount.setText("累计金额：¥" + RxBigDecimalTool.toDecimal(totalAmount.getTotalRevenue(), 2));
        }
    }

    /**
     * 设置合伙人
     *
     * @param id
     */
    private void setPartner(int id) {
        OkGo.<AMBaseDto<Empty>>get(Constants.setPartnerUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("user_id", id)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            refresh();
                            EventBus.getDefault().post(new MessageEvent("setPartnerSuccess"));
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
