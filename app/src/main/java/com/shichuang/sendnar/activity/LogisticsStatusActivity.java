package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.LogisticsStatusAdapter;
import com.shichuang.sendnar.adapter.OrderInfoAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.Convert;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GiftsDetails;
import com.shichuang.sendnar.entify.LogisticsInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流状态
 * Created by Administrator on 2018/4/23.
 */

public class LogisticsStatusActivity extends BaseActivity {
    private View mHeaderView;
    private TextView mTvLogisticsOrder;
    private TextView mTvLogisticsCompany;
    private RecyclerView mRecyclerView;
    private LogisticsStatusAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    // 我的订单，订单详情页面使用
    private String orderId;
    // 其他界面
    private String logisticsNo;
    private String logisticsCompany;

    @Override
    public int getLayoutId() {
        return R.layout.activity_logistics_status;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderId = getIntent().getStringExtra("orderId");
        logisticsNo = getIntent().getStringExtra("logisticsNo");
        logisticsCompany = getIntent().getStringExtra("logisticsCompany");
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.layout_logistics_status_header, (ViewGroup) findViewById(android.R.id.content), false);
        mTvLogisticsOrder = mHeaderView.findViewById(R.id.tv_logistics_order);
        mTvLogisticsCompany = mHeaderView.findViewById(R.id.tv_logistics_company);
        initRecyclerView();
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LogisticsStatusAdapter();
        mAdapter.addHeaderView(mHeaderView);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        getLogisticsData();
    }

    private void getLogisticsData() {
        OkGo.<AMBaseDto<String>>get(Constants.getLogisticsMsgUrl)
                .tag(mContext)
                .params("order_id", orderId)
                .params("express_no", logisticsNo)
                .params("com", logisticsCompany)
                .execute(new NewsCallback<AMBaseDto<String>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<String>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<String>> response) {
                        if (response.body().code == 0) {
                            String info = response.body().data;
                            if (!TextUtils.isEmpty(info)) {
                                LogisticsInformation logisticsInformation = Convert.fromJson(info, LogisticsInformation.class);
                                if (logisticsInformation != null) {
                                    if (logisticsInformation.getData() != null && logisticsInformation.getData().size() > 0) {
                                        handleData(logisticsInformation);
                                        mEmptyLayout.hide();
                                    } else {
                                        mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                                    }
                                } else {
                                    mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                                }
                            } else {
                                mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<String>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 绑定物流信息
     */

    private void handleData(LogisticsInformation logisticsInformation) {
        mTvLogisticsOrder.setText("物流单号：" + logisticsInformation.getNu());
        mTvLogisticsCompany.setText("物流公司：" + logisticsInformation.getCom());

        mAdapter.replaceData(logisticsInformation.getData());
    }
}
