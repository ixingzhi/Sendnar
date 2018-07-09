package com.shichuang.sendnar.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.GiftsCategoryActivity;
import com.shichuang.sendnar.adapter.GiftsCategoryType1Adapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.entify.GiftsCategoryType1;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GiftsCategoryType1Fragment extends BaseFragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private GiftsCategoryType1Adapter mAdapter;

    private int typeId;
    private int priceTypeId;

    private ExchangeGift exchangeGift;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gifts_category_type_1;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        typeId = getArguments().getInt("typeId");
        priceTypeId = getArguments().getInt("priceTypeId");
        exchangeGift = (ExchangeGift) getArguments().getSerializable("exchangeGift");

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mAdapter = new GiftsCategoryType1Adapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("giftObjectId", mAdapter.getItem(position).getId());
                bundle.putInt("typeId", typeId);
                bundle.putInt("priceTypeId", priceTypeId);
                bundle.putSerializable("exchangeGift", exchangeGift);
                RxActivityTool.skipActivity(mContext, GiftsCategoryActivity.class, bundle);
            }
        });
    }

    @Override
    public void initData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    private void getData() {
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
                            mAdapter.replaceData(response.body().data);
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
}
