package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.OrderDetailsAdapter;
import com.shichuang.sendnar.entify.OrderDetails;

/**
 * 选择服务类型
 * Created by Administrator on 2018/6/5.
 */

public class ChooseServiceTypeActivity extends BaseActivity {
    // 商品列表
    private RecyclerView mRecyclerView;
    private OrderDetailsAdapter mAdapter;

    private OrderDetails.OrderDetailsGoodsListModel orderInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_service_type;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderInfo = (OrderDetails.OrderDetailsGoodsListModel) getIntent().getSerializableExtra("orderInfo");
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrderDetailsAdapter();
        mAdapter.isOrderDetailsPage(false);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void initEvent() {
        findViewById(R.id.rl_refund).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderInfo", orderInfo);
                bundle.putInt("operateType",1);   // 1-退款 2-退款退货 3-换货
                RxActivityTool.skipActivity(mContext, RefundReturnGoodsActivity.class, bundle);
                finish();
            }
        });
        findViewById(R.id.rl_refund_and_return_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderInfo", orderInfo);
                bundle.putInt("operateType",2);   // 1-退款 2-退款退货 3-换货
                RxActivityTool.skipActivity(mContext, RefundReturnGoodsActivity.class, bundle);
                finish();
            }
        });
        findViewById(R.id.rl_exchange_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderInfo", orderInfo);
                bundle.putInt("operateType",3);   // 1-退款 2-退款退货 3-换货
                RxActivityTool.skipActivity(mContext, ApplyExchangeGoodsActivity.class, bundle);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        if (orderInfo != null) {
            mAdapter.addData(orderInfo);
        }
    }
}
