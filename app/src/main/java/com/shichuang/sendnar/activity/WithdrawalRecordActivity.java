package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.WithdrawalRecordAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Home;
import com.shichuang.sendnar.entify.MyOrder;
import com.shichuang.sendnar.entify.WithdrawalRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class WithdrawalRecordActivity extends BaseActivity {
    private RxEmptyLayout mEmptyLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private WithdrawalRecordAdapter mAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_withdrawal_record;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WithdrawalRecordAdapter(null);
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
    }

    @Override
    public void initData() {
        refresh();
    }

    private void refresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });
    }

    private void getData() {
        OkGo.<AMBaseDto<List<WithdrawalRecord>>>get(Constants.withdrawalRecordUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<List<WithdrawalRecord>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<WithdrawalRecord>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<WithdrawalRecord>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                            mEmptyLayout.hide();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<WithdrawalRecord>>> response) {
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

    private void handleData(List<WithdrawalRecord> data) {
        List<WithdrawalRecord> mList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            WithdrawalRecord withdrawalRecordHeader = new WithdrawalRecord(WithdrawalRecord.WITHDRAWAL_HEADER);
            withdrawalRecordHeader.setTime(data.get(i).getTime());
            mList.add(withdrawalRecordHeader);

            List<WithdrawalRecord.MonthWithdrawalData> monthDataList = data.get(i).getMonthData();
            for (int j = 0; j < monthDataList.size(); j++) {
                WithdrawalRecord withdrawalRecordBody = new WithdrawalRecord(WithdrawalRecord.WITHDRAWAL_BODY);
                withdrawalRecordBody.setMonthData(monthDataList);
                withdrawalRecordBody.setPosition(j);
                mList.add(withdrawalRecordBody);
            }
        }
        mAdapter.replaceData(mList);
    }


}
